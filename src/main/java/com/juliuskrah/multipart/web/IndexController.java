/*
* Copyright 2016, Julius Krah
* by the @authors tag. See the LICENCE in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.juliuskrah.multipart.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.juliuskrah.multipart.entity.Account;

@Controller
public class IndexController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("account", new Account());

		return "index";
	}

	@PostMapping("/")
	public String index(@ModelAttribute Account account, @RequestParam("file") MultipartFile file, Errors errors) {
		if (errors.hasErrors()) {
			return "index";
		}
		log.debug("Filename is {}", file.getOriginalFilename());

		return "redirect:/";
	}
}
