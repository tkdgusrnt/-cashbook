package com.gdu.cashbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.gdu.cashbook.service.CommentService;

@Controller
public class CommnetController {
	@Autowired CommentService commentService;
}
