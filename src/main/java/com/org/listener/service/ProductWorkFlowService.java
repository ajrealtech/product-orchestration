package com.org.listener.service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.listener.data.ProductRepository;
import com.org.listener.data.ProductWorkFlow;
import com.org.listener.data.ProductWorkflowRepository;
import com.org.listener.dto.Product;
import com.org.listener.dto.ProductSearch;

@Service
public class ProductWorkFlowService {
	
	
	@Autowired
	private ProductRepository productRepository;
	

	@Autowired
	private ProductWorkflowRepository productFlowRepository;
	
	@Value( "${martek.central.uri}" )
	private String martekUri;
	
	Logger log = LoggerFactory.getLogger(ProductWorkFlowService.class);
	
	
	public void processWorkflow(ProductSearch list ) {
		try {
		List<Product> response = new ArrayList();
		List<String> names = list.getProduct().stream().map(message -> message.getName()).collect(Collectors.toList());
		Product product = new Product();
		
		//List ry = productFlowRepository.getWorkflowfromId(list.getFlowId());
		List<com.org.listener.data.Product> productData = productRepository.findByNameIn(names);
		List<String> localnames = productData.stream().map(message -> message.getName()).collect(Collectors.toList());
		productData.stream().forEach(e->{Product res = new Product();res.setId(e.getId());res.setInventoryType("Local");res.setLocation(e.getLocation());
		res.setName(e.getName());response.add(res);});
		//localnames.stream().forEach(e->{Product res = new Product();res.setInventoryType("Local");response.add(res);});
		List<String> nonLocal = names.stream().filter(e->!localnames.contains(e)).collect(Collectors.toList());
		if(nonLocal.size()!=0) {
			nonLocal.stream().forEach(e->{Product res = new Product();res.setInventoryType("Central");response.add(res);});
				
		}
		publishWorkflow(list.getFlowId(),response);
		System.out.print(productData);
		}catch(Exception e) {
			//final String uri = attendenceUri+message.getId();
			System.out.println("AMIT:"+e);
		    //RestTemplate restTemplate = new RestTemplate();
		    //String result = restTemplate.getForObject(uri, String.class);
		    //System.out.println("AMIT:"+result);
		    //log.info("AMIT:"+result);
		//	throw e;
		}
	
		
	}
		
	private void publishWorkflow(String fid,List<Product> list) {
		String message="";
		try {
			 message = new ObjectMapper().writeValueAsString(list);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ProductWorkFlow productFlow = productFlowRepository.getSearch(fid,"SEARCH_PROGRESS");
		productFlow.setMessage(message);
		productFlowRepository.save(productFlow);
		productFlow.setStatus("SEARCH_COMPLETE");
		productFlowRepository.saveAndFlush(productFlow);
	}
}
