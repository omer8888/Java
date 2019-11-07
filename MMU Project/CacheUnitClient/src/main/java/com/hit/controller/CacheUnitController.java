package com.hit.controller;

import java.util.Observable;
import java.util.Observer;
import com.hit.model.Model;
import com.hit.view.View;

public class CacheUnitController implements Controller , Observer {
	
	private Model model;
	private View view;
	
	public CacheUnitController(Model model, View view) { // c'tor
		this.model = model;
		this.view = view;
	}
	
	@Override
	public void update(Observable obs, Object obj) {
		
		System.out.println("In: CacheUnitController 'update'");
		
		if(obs instanceof View) {
			System.out.println("In: CacheUnitController instanceof View");
			System.out.println(obj);
			model.updateModelData(obj);
		}
		else if(obs instanceof Model) {
			System.out.println("In: CacheUnitController instanceof Model");
			System.out.println(obj);
			view.updateUIData(obj);
		}
	}
}
