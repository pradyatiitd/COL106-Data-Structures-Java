import java.util.*;

class AVLTNode{
	String key;
	int height;
	AVLTNode left;
	AVLTNode right;
	ArrayList<String> value;

	public AVLTNode(String data){
		this.key = data;
		this.height = 1;
		this.left = null;
		this.right = null;
		this.value = new ArrayList<String>();
	}
}

public class AVLTree{
	AVLTNode root;

	public AVLTree(){
		this.root = null;
	}

	public int height(AVLTNode node){
		if(node==null){
			return 0;
		}
		return node.height;
	}

	public int getBalance(AVLTNode n){
		if(n==null){
			return 0;
		}
		return this.height(n.left)-this.height(n.right);
	}

	public boolean containsKey(AVLTNode node,String key){
		if(node==null){
			return false;
		}
		int val = node.key.compareTo(key);
		if(val==0){
			return true;
		}else if(val<0){
			return this.containsKey(node.right,key);
		}else{
			return this.containsKey(node.left,key);
		}
	}

	public void print(AVLTNode node){
		if(node==null){
			return;
		}
		System.out.println(node.key);
		this.print(node.left);
		this.print(node.right);
	}

	public AVLTNode rightRotate(AVLTNode n0){
		AVLTNode n1 = n0.left;
		AVLTNode n2 = n1.right;

		n1.right = n0;
		n0.left = n2;

		n0.height = 1 + Math.max(this.height(n0.left),this.height(n0.right));
		n1.height = 1 + Math.max(this.height(n1.left),this.height(n1.right));

		return n1;
	}

	public AVLTNode leftRotate(AVLTNode n0){
		AVLTNode n1 = n0.right;
		AVLTNode n2 = n1.left;

		n1.left = n0;
		n0.right = n2;

		n0.height = 1 + Math.max(this.height(n0.left),this.height(n0.right));
		n1.height = 1 + Math.max(this.height(n1.left),this.height(n1.right));

		return n1;
	}

	public AVLTNode insert(AVLTNode node,String key,String val){
		if(node==null){
			AVLTNode n = new AVLTNode(key);
			n.value.add(val);
			return n;
		}

		int diff = node.key.compareTo(key);
		if(diff>0){
			node.left = this.insert(node.left,key,val);
		}else if(diff<0){
			node.right = this.insert(node.right,key,val);
		}else{
			node.value.add(val);
			return node;
		}

		node.height = 1 + Math.max(this.height(node.left),this.height(node.right));
		int bal = this.getBalance(node);
		int leftdiff = 0,rightdiff = 0;

		if(bal<-1){
			rightdiff = node.right.key.compareTo(key);
		}

		if(bal>1){
			leftdiff = node.left.key.compareTo(key);
		}

		if(bal>1 && leftdiff>0){
			return this.rightRotate(node);
		}

		if(bal<-1 && rightdiff<0){
			return this.leftRotate(node);
		}

		if(bal>1 && leftdiff<0){
			node.left = this.leftRotate(node.left);
			return this.rightRotate(node);
		}

		if(bal<-1 && rightdiff>0){
			node.right = this.rightRotate(node.right);
			return this.leftRotate(node);
		}

		return node;
	}

	public void put(String key,String val){
		this.root = this.insert(this.root,key,val);
	}

	public ArrayList<String> get(AVLTNode node,String test){
		int val = node.key.compareTo(test);
		if(val==0){
			return node.value;
		}else if(val<0){
			return this.get(node.right,test);
		}else{
			return this.get(node.left,test);
		}
	}
}