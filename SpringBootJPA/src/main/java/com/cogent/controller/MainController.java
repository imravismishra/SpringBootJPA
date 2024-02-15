package com.cogent.controller;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cogent.entity.CourseEntity;
import com.cogent.entity.NameEntity;
import com.cogent.entity.UserEntity;
import com.cogent.repository.CourseRepository;
import com.cogent.repository.UserRepository;

import lombok.RequiredArgsConstructor;

//used for web services - is a method of providing data to front end
//restful api - JSON - javascript object notation
//soap API - xml - xtensible marker language

//@RestController
@RequiredArgsConstructor
@Controller
public class MainController {
	private final UserRepository userRepository;
	private final CourseRepository courseRepository;

	@Value("${mypath}")
	private String path;

//	@RequestMapping(method = RequestMethod.GET, path = {"/"})
	@GetMapping(path = { "/" })
	@ResponseBody
	public List<UserEntity> getAllData() {
		List<UserEntity> allRecords = userRepository.findAll();
		return allRecords;
	}

	@PostMapping(path = { "/sorted/{field}" })
	@ResponseBody
	public List<UserEntity> getAllSortedData(@PathVariable("field") String field) {
		List<UserEntity> allRecords = userRepository.findAll(Sort.by(Sort.Direction.ASC, field));
		return allRecords;
	}

	@GetMapping(path = { "/{id}" })
	@ResponseBody
	public UserEntity getRecordById(@PathVariable("id") int id) {
		UserEntity record = userRepository.findById(id).orElse(new UserEntity());
		return record;
	}

	@PostMapping(path = { "/name" })
	@ResponseBody
	public List<UserEntity> getRecordByName(@RequestBody NameEntity name) {
		String fname = name.getFname();
		System.out.println(name);
		List<UserEntity> data = userRepository.findByFname(fname).orElse(null);
		return data;
	}

	@GetMapping(path = { "/name/{name}" })
	@ResponseBody
	public List<UserEntity> getRecordByNameKeyword(@PathVariable("name") String name) {
		System.out.println(name);
		List<UserEntity> data = userRepository.findByNameKeyword(name);
		return data;
	}

	@PostMapping(path = { "/verify" })
	@ResponseBody
	public boolean verifyUserByEmailandPassword(@RequestParam("email") String email,
			@RequestParam("password") String password) {

		System.out.println(email);
		System.out.println(password);

		return userRepository.findByEmailandPassword(email, password).isPresent() ? true : false;

	}

	@PostMapping(path = { "/insert" }, consumes = { "application/json" }, produces = { "application/json" })
	@ResponseBody
	public UserEntity insert(@RequestBody UserEntity userEntity) {
		System.out.println(userEntity);
		List<CourseEntity> listCourses = userEntity.getCourses();
		List<CourseEntity> finalcourses = new ArrayList<CourseEntity>();

		for (CourseEntity course : listCourses) {
			CourseEntity c = courseRepository.findByCourse(course.getCourse());
			if (c != null) {
				finalcourses.add(c);
			} else {
				finalcourses.add(course);
			}

		}

		userEntity.setCourses(finalcourses);

		return userRepository.save(userEntity);

	}

	@PutMapping(path = { "/" }) // putmapping is for updation
	@ResponseBody
	public String updateRecord(@RequestBody UserEntity userEntity) {

		Optional<UserEntity> newUserEntity = userRepository.findByEmailandPassword(userEntity.getEmail(),
				userEntity.getPassword());

		if (newUserEntity.isPresent()) {
			newUserEntity.get().setName(userEntity.getName());
			newUserEntity.get().setPassword(userEntity.getPassword());
			newUserEntity.get().setCourses(userEntity.getCourses());
			String message = !userRepository.save(newUserEntity.get()).getEmail().isBlank() ? "Data is updated"
					: "Data is not updated";
			return message;

		} else {
			return "Password is invalid";
		}

	}

	@PostMapping(path = { "/upload" })
	@ResponseBody
	public boolean uploadImage(@RequestParam("file") MultipartFile file) {
		System.out.println(path);
		return MyUploadImage(file);
	}

	private boolean MyUploadImage(MultipartFile file) {

		path = path + file.getOriginalFilename();
		try {
			FileOutputStream fout = new FileOutputStream(path);
			BufferedOutputStream bout = new BufferedOutputStream(fout);
			bout.write(file.getBytes());
			bout.flush();
			bout.close();
			fout.flush();
			fout.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}

}
