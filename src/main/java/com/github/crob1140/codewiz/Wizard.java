package com.github.crob1140.codewiz;

import java.util.Stack;

import com.github.crob1140.codewiz.actions.Action;
import com.github.crob1140.codewiz.actions.MoveAction;

public class Wizard {
	
	private Stack<Action> actionStack;
	
	public void onIdle(){}
	public void onEnemySighted(){}
	
	protected void move(int amount) {
		System.out.println("Moving " + amount + " units");
		this.actionStack.push(new MoveAction(amount));
	}

	public void setActionQueue(Stack<Action> actionStack) {
		this.actionStack = actionStack;
	}
}
