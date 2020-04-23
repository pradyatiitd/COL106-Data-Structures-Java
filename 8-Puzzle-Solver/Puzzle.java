import java.util.*;
import java.io.*;

class Node{
	public int cost;
	public int moves;
	public Node parent;
	public String move;
	public String state;

	public Node(int cost,int moves,Node node,String move,String str){
		this.cost = cost;
		this.moves = moves;
		this.parent = node;
		this.move = move;
		this.state = str;
	}

	public boolean compare(Node node){
		if(this.cost!=node.cost){
			return this.cost<node.cost;
		}else if(this.moves!=node.moves){
			return this.moves<node.moves;
		}
		return true;
	}
}

class Heap{
	public ArrayList<Node> list;
	public int size;

	public Heap(){
		this.list = new ArrayList<Node>();
		this.size = 0;
	}

	public void add(Node n){
		this.list.add(this.size,n);
		this.size+=1;
		this.upHeapify(this.size-1);
	}
	
	public Node get(){
		Node node = this.list.get(0);
		this.list.set(0,this.list.get(this.size-1));
		this.size-=1;
		this.downHeapify(0);
		return node;
	}

	public void upHeapify(int idx){
		int parent = idx/2;
		if(parent<idx && this.list.get(idx).compare(this.list.get(parent))){
			Node n = this.list.get(idx);
			this.list.set(idx,this.list.get(parent));
			this.list.set(parent,n);
			this.upHeapify(parent);
		}
	}

	public void downHeapify(int idx){
		int index = idx;
		int lc = 2*idx+1;
		int rc = 2*idx+2;

		if(lc<this.size && this.list.get(lc).compare(this.list.get(index))){
			index = lc;
		}

		if(rc<this.size && this.list.get(rc).compare(this.list.get(index))){
			index = rc;
		}

		if(index!=idx){
			Node n = this.list.get(idx);
			this.list.set(idx,this.list.get(index));
			this.list.set(index,n);
			this.downHeapify(index);
		}
	}
}

public class Puzzle{
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		BufferedWriter bw = new BufferedWriter(new FileWriter(args[1]));
		int t = Integer.parseInt(br.readLine());
		
		int[] cost_array;
		String[] str_array;
		
		while(t!=0){
			str_array = br.readLine().split(" ");
			String start = str_array[0];
			String goal = str_array[1];
			
			str_array = br.readLine().split(" ");
			cost_array = new int[8];
			for(int idx = 0;idx<8;idx++){
				cost_array[idx] = Integer.parseInt(str_array[idx]);
			}

			if(isSolvable(start,goal)==false){
				bw.write("-1 -1\n\n");
				t-=1;
				continue;
			}

			HashMap<String,Node> stateMap = new HashMap<String,Node>();
			ArrayList<Node> nextMoves = new ArrayList<Node>();
			Node node = new Node(0,0,null,"",start);
			Heap heap = new Heap();

			heap.add(node);
			stateMap.put(start,node);

			while(true){
				node = heap.get();
				if(node.state.compareTo(goal)==0){
					break;
				}

				nextMoves = new ArrayList<Node>();
				generateMoves(node,cost_array,nextMoves);
				for(Node n: nextMoves){
					if(stateMap.containsKey(n.state)==false || (stateMap.get(n.state).compare(n)==false)){
						stateMap.put(n.state,n);
						heap.add(n);
					}
				}
			}

			ArrayList<String> pathList = new ArrayList<String>();
			node = stateMap.get(goal);
			bw.write(node.moves + " " + node.cost + "\n");

			while(node.parent!=null){
				pathList.add(node.move);
				node = node.parent;
			}

			for(int i = pathList.size()-1;i>0;i--){
				bw.write(pathList.get(i) + " ");
			}

			if(pathList.size()>0){
				bw.write(pathList.get(0));
			}

			bw.write("\n");
			t-=1;
		}

		br.close();
		bw.close();
	}

	public static boolean isSolvable(String start, String goal){
		int i1 = 0,i2 = 0;
		int[] arr1 = new int[9];
		int[] arr2 = new int[9];

		for(int i = 0;i<9;i++){
			if(start.charAt(i)!='G'){
				arr1[i] = start.charAt(i)-'0';
			}

			if(goal.charAt(i)!='G'){
				arr2[i] = goal.charAt(i)-'0';
			}
		}

		for(int i = 0;i<9;i++){
			for(int j = i+1;j<9;j++){
				if(arr1[i]>0 && arr1[j]>0 && arr1[i]>arr1[j]){
					i1++;
				}

				if(arr2[i]>0 && arr2[j]>0 && arr2[i]>arr2[j]){
					i2++;
				}
			}
		}

		return i1%2 == i2%2;
	}

	public static void generateMoves(Node n,int[] cost_arr,ArrayList<Node> list){
		int i = 0,j = 0;
		for(int index = 0;index<9;index++){
			if(n.state.charAt(index)=='G'){
				i = index/3;
				j = index%3;
				break;
			}
		}

		char[] stateArr;
		String temp;
		Node node;
		char ch;
		int idx;

		// Right move
		if(j>0){
			stateArr = n.state.toCharArray();
			ch = stateArr[3*i+j-1];
			idx = ch - '1';
			stateArr[3*i+j-1] = 'G';
			stateArr[3*i + j] = ch;
			temp = new String(stateArr);
			node = new Node(n.cost+cost_arr[idx],n.moves+1,n,Integer.toString(idx+1) + "R",temp);
			list.add(node);
		}

		// Down move
		if(i>0){
			stateArr = n.state.toCharArray();
			ch = stateArr[3*(i-1) + j];
			idx = ch - '1';
			stateArr[3*(i-1)+j] = 'G';
			stateArr[i*3 + j] = ch;
			temp = new String(stateArr);
			node = new Node(n.cost+cost_arr[idx],n.moves+1,n,Integer.toString(idx+1) + "D",temp);
			list.add(node);
		}

		// Left move
		if(j<2){
			stateArr = n.state.toCharArray();
			ch = stateArr[3*i+j+1];
			idx = ch - '1';
			stateArr[3*i+j+1] = 'G';
			stateArr[3*i + j] = ch;
			temp = new String(stateArr);
			node = new Node(n.cost+cost_arr[idx],n.moves+1,n,Integer.toString(idx+1) + "L",temp);
			list.add(node);
		}

		// Up move
		if(i<2){
			stateArr = n.state.toCharArray();
			ch = stateArr[3*(i+1) + j];
			idx = ch - '1';
			stateArr[3*(i+1)+j] = 'G';
			stateArr[i*3 + j] = ch;
			temp = new String(stateArr);
			node = new Node(n.cost+cost_arr[idx],n.moves+1,n,Integer.toString(idx+1) + "U",temp);
			list.add(node);
		}
	}
}