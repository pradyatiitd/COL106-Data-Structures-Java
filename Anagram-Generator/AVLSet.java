import java.util.*;

class AVLSNode{
	String key;
	int height;
	AVLSNode left;
	AVLSNode right;

	public AVLSNode(String data){
		this.key = data;
		this.height = 1;
		this.left = null;
		this.right = null;
	}
}

public class AVLSet{
	AVLSNode root;

	public AVLSet(){
		this.root = null;
	}

	public int height(AVLSNode node){
		if(node==null){
			return 0;
		}
		return node.height;
	}

	public int getBalance(AVLSNode n){
		if(n==null){
			return 0;
		}
		return this.height(n.left)-this.height(n.right);
	}

	public boolean containsKey(AVLSNode node,String key){
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

	public AVLSNode rightRotate(AVLSNode n0){
		AVLSNode n1 = n0.left;
		AVLSNode n2 = n1.right;

		n1.right = n0;
		n0.left = n2;

		n0.height = 1 + Math.max(this.height(n0.left),this.height(n0.right));
		n1.height = 1 + Math.max(this.height(n1.left),this.height(n1.right));

		return n1;
	}

	public AVLSNode leftRotate(AVLSNode n0){
		AVLSNode n1 = n0.right;
		AVLSNode n2 = n1.left;

		n1.left = n0;
		n0.right = n2;

		n0.height = 1 + Math.max(this.height(n0.left),this.height(n0.right));
		n1.height = 1 + Math.max(this.height(n1.left),this.height(n1.right));

		return n1;
	}

	public void add(String key){
		this.root = this.insert(this.root,key);
	}

	public AVLSNode insert(AVLSNode node,String key){
		if(node==null){
			AVLSNode n = new AVLSNode(key);
			return n;
		}

		int diff = node.key.compareTo(key);
		if(diff>0){
			node.left = this.insert(node.left,key);
		}else if(diff<0){
			node.right = this.insert(node.right,key);
		}else{
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

	public void get(AVLSNode node,ArrayList<String> ans){
		if(node==null){
			return;
		}
		this.get(node.left,ans);
		ans.add(node.key);
		this.get(node.right,ans);
	}
}