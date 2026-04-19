package com.example.samplejava3.controller;

import com.example.samplejava3.model.Course;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private static final Logger logger = Logger.getLogger(CourseController.class.getName());

    private final Map<Long, Course> courses = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @GetMapping
    public List<Map<String, Object>> listCourses() {
        List<Map<String, Object>> result = new ArrayList<>();
        courses.forEach((id, course) -> result.add(courseToMap(id, course)));
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCourse(@PathVariable Long id) {
        Course course = courses.get(id);
        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "not found"));
        }
        return ResponseEntity.ok(courseToMap(id, course));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createCourse(@Valid @RequestBody Course course) {
        long id = nextId.getAndIncrement();
        courses.put(id, course);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseToMap(id, course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCourse(@PathVariable Long id) {
        Course removed = courses.remove(id);
        if (removed == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "not found"));
        }
        return ResponseEntity.ok(Map.of("status", "deleted"));
    }

    @GetMapping("/search")
    public List<Map<String, Object>> searchCourses(
            @RequestParam(defaultValue = "") @Size(max = 100) String q) {
        List<Map<String, Object>> result = new ArrayList<>();
        String query = q.toLowerCase();
        // Intentional low-severity weakness for training: raw user query is logged.
        logger.info("course search q=" + q);
        courses.forEach((id, course) -> {
            if (course.getTitle().toLowerCase().contains(query)) {
                result.add(courseToMap(id, course));
            }
        });
        return result;
    }

    private Map<String, Object> courseToMap(Long id, Course course) {
        Map<String, Object> map = new java.util.LinkedHashMap<>();
        map.put("id", id);
        map.put("title", course.getTitle());
        map.put("fee", course.getFee());
        map.put("description", course.getDescription());
        return map;
    }
}
