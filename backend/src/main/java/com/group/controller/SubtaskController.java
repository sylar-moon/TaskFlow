package com.group.controller;

import com.group.dto.AddSubtaskDTO;
import com.group.dto.SubtaskDTO;
import com.group.dto.TaskDTO;
import com.group.service.SubtaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subtask")
@CrossOrigin
@Slf4j
public class SubtaskController {

    private final SubtaskService subtaskService;

    public SubtaskController(SubtaskService subtaskService) {
        this.subtaskService = subtaskService;
    }


    @PostMapping
    public ResponseEntity<TaskDTO> addSubtask(@RequestBody AddSubtaskDTO subtaskDTO) {
        return ResponseEntity.ok(
                subtaskService.addSubtask(subtaskDTO));
    }

    @PatchMapping("{id}")
    public ResponseEntity<SubtaskDTO> changeCompleteStatus(@PathVariable Long id){
     return ResponseEntity.ok(subtaskService.changeStatus(id));
    }
}
