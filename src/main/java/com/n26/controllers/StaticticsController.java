package com.n26.controllers;

import com.n26.dto.StatisticsDto;
import com.n26.service.StaticticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * REST Controller for all statictics related
 */
@Controller(value = "/statistics")
public class StaticticsController {


    @Autowired
    private StaticticService staticticService;

    /**
     * Method to get statistics.
     * @return StatisticsDto
     */
    @GetMapping()
    public ResponseEntity<?> getStatistics () {
        return new ResponseEntity<StatisticsDto>(staticticService.getStatistics(), HttpStatus.OK);
    }

}
