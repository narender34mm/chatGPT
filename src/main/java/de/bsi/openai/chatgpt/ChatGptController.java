package de.bsi.openai.chatgpt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.bsi.openai.FormInputDTO;
import de.bsi.openai.OpenAiApiClient;
import de.bsi.openai.OpenAiApiClient.OpenAiService;

@RestController
@CrossOrigin("*")
public class ChatGptController {
	
	private static final String MAIN_PAGE = "";
	
	@Autowired private ObjectMapper jsonMapper;
	@Autowired private OpenAiApiClient client;
	
	private String chatWithGpt3(String message) throws Exception {
		var completion = CompletionRequest.defaultWith(message);
		var postBodyJson = jsonMapper.writeValueAsString(completion);
		var responseBody = client.postToOpenAiApi(postBodyJson, OpenAiService.GPT_3);
		var completionResponse = jsonMapper.readValue(responseBody, CompletionResponse.class);
		System.out.println("com"+completionResponse);
		return completionResponse.firstAnswer().orElseThrow();
	}
	
	
	@PostMapping(path = "/")
	public String chat(Model model, @RequestParam String dto) throws Exception {
		try {
			model.addAttribute("request", dto);
			model.addAttribute("response", chatWithGpt3(dto));
		} catch (Exception e) {
			model.addAttribute("response", "Error in communication with OpenAI ChatGPT API.");
		}
		return chatWithGpt3(dto);
	}
	
}