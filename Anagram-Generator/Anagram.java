import java.util.*;
import java.io.*;

public class Anagram{
	
	public static void main(String[] args) throws FileNotFoundException, IOException{
		long start = System.currentTimeMillis();
		
		// Generating hashmaps of binary strings
		ArrayList<Integer> vals = new ArrayList<Integer>();
		ArrayList<ArrayList<String>> stringMap = new ArrayList<ArrayList<String>>();

		vals.add(3);
		binary(6,vals,stringMap);
		binary(7,vals,stringMap);

		vals.add(4);
		binary(8,vals,stringMap);
		binary(9,vals,stringMap);

		vals.add(5);
		binary(10,vals,stringMap);
		binary(11,vals,stringMap);

		vals.add(6);
		binary(12,vals,stringMap);

		// generate hashmap for anagrams of dictionary words
		AVLTree dict_map = new AVLTree();
		Scanner scn1 = new Scanner(new File(args[0]));
		generate_map(scn1,dict_map);
		scn1.close();

		AVLSet ans = new AVLSet();
		ArrayList<String> ansList = new ArrayList<String>();
		
		Scanner scn2 = new Scanner(new File(args[1]));
		int num_queries = scn2.nextInt();
		
		char[] hash_arr;
		String test = "";
		String test_hash = "";

		FileWriter fw = new FileWriter("anagram_2.txt");

		// Reading the queries
		for(int i = 0;i<num_queries;i++){
			test = scn2.next();
			hash_arr = test.toCharArray();
			Arrays.sort(hash_arr);
			test_hash = new String(hash_arr);

			ans = new AVLSet();
			if(dict_map.containsKey(dict_map.root,test_hash)){
				ansList = dict_map.get(dict_map.root,test_hash);
				for(String og_string: ansList){
					ans.add(og_string);
				}
				ansList = new ArrayList<String>();
			}

			if(test.length()==6){
				TwoPartitions(test,ans,stringMap,dict_map);
			}else if(test.length()==7){
				TwoPartitions(test,ans,stringMap,dict_map);
			}else if(test.length()==8){
				TwoPartitions(test,ans,stringMap,dict_map);
			}else if(test.length()==9){
				TwoPartitions(test,ans,stringMap,dict_map);
				ThreePartitions(test,ans,dict_map,stringMap,3,3);
			}else if(test.length()==10){
				TwoPartitions(test,ans,stringMap,dict_map);
				ThreePartitions(test,ans,dict_map,stringMap,3,3);
			}else if(test.length()==11){
				TwoPartitions(test,ans,stringMap,dict_map);
				ThreePartitions(test,ans,dict_map,stringMap,3,3);
				ThreePartitions(test,ans,dict_map,stringMap,3,4);
			}else if(test.length()==12){
				TwoPartitions(test,ans,stringMap,dict_map);
				ThreePartitions(test,ans,dict_map,stringMap,3,3);
				ThreePartitions(test,ans,dict_map,stringMap,3,4);
				ThreePartitions(test,ans,dict_map,stringMap,4,4);
			}

			ans.get(ans.root,ansList);
			for(String soln: ansList){
				fw.write(soln + "\n");
			}
			fw.write("-1\n");
		}
		fw.close();
		scn2.close();
		long end = System.currentTimeMillis(); 
        System.out.println("Time taken " + (end - start) + "ms");
	}

	public static ArrayList<String> getStringList(int length,int count,ArrayList<ArrayList<String>> stringMap){
		if(length<=7){
			return stringMap.get(length-6);
		}else if(length==8){
			return stringMap.get(count-1);
		}else if(length==9){
			return stringMap.get(count+1);
		}else if(length==10){
			return stringMap.get(count+3);
		}else if(length==11){
			return stringMap.get(count+6);
		}else{
			return stringMap.get(count+9);
		}
	}

	public static void addString(int length,int count,String s,ArrayList<ArrayList<String>> stringMap){
		if(length<=7){
			stringMap.get(length-6).add(s);
		}else if(length==8){
			stringMap.get(count-1).add(s);
		}else if(length==9){
			stringMap.get(count+1).add(s);
		}else if(length==10){
			stringMap.get(count+3).add(s);
		}else if(length==11){
			stringMap.get(count+6).add(s);
		}else{
			stringMap.get(count+9).add(s);
		}
	}

	public static void generate_map(Scanner scn, AVLTree map){
		int num_dict_words = scn.nextInt();
		String str = "";
		String hash = "";
		char[] hash_arr;
		for(int i = 0;i<num_dict_words;i++){
			str = scn.next();
			hash_arr = str.toCharArray();
			Arrays.sort(hash_arr);
			hash = new String(hash_arr);
			map.put(hash,str);
		}
	}

	public static void binary(int n,ArrayList<Integer> list,ArrayList<ArrayList<String>> stringMap){
		for(int i = 0;i<list.size();i++){
			ArrayList<String> temp = new ArrayList<String>();
			stringMap.add(temp);
			binary_helper(n,0,list.get(i),"",stringMap);
		}
	}

	public static void binary_helper(int length,int count,int limit,String curr,ArrayList<ArrayList<String>> stringMap){
		if(count>limit || curr.length()>length){
			return;
		}else if(curr.length()==length && count==limit){
			addString(length,count,curr,stringMap);
		}
		String s1 = curr + "0";
		String s2 = curr + "1";
		binary_helper(length,count,limit,s1,stringMap);
		binary_helper(length,count+1,limit,s2,stringMap);
	}

	public static void TwoPartitions(String str,AVLSet ans,ArrayList<ArrayList<String>> stringMap,AVLTree dict_map){
		ArrayList<Integer> count_list = new ArrayList<Integer>();
		ArrayList<String> helper = new ArrayList<String>();
		StringBuilder sb1,sb2;
		String s1,s2;

		count_list.add(3);
		if(str.length()>=8){
			count_list.add(4);
		}

		if(str.length()>=10){
			count_list.add(5);
		}

		if(str.length()==12){
			count_list.add(6);
		}

		for(int i = 0;i<count_list.size();i++){
			helper = getStringList(str.length(),count_list.get(i),stringMap);
			for(String binary:helper){
				sb1 = new StringBuilder();
				sb2 = new StringBuilder();
				for(int idx = 0;idx<binary.length();idx++){
					if(binary.charAt(idx)=='1'){
						sb1.append(str.charAt(idx));
					}else{
						sb2.append(str.charAt(idx));
					}
				}

				s1 = sb1.toString();
				s2 = sb2.toString();
				if(dict_map.containsKey(dict_map.root,s1) && dict_map.containsKey(dict_map.root,s2)){
					combine(s1,s2,ans,dict_map);
				}
			}
		}
	}

	public static void ThreePartitions(String str,AVLSet ans,AVLTree dict_map,ArrayList<ArrayList<String>> stringMap,int n1,int n2){
		ArrayList<String> binary1,binary2;
		StringBuilder sb1,sb2,sb3,sb4;
		String s1,s2,s3,s4;
		char[] hash_arr;

		binary1 = getStringList(str.length(),n1,stringMap);
		for(String s:binary1){
			sb1 = new StringBuilder();
			sb2 = new StringBuilder();
			for(int idx = 0;idx<s.length();idx++){
				if(s.charAt(idx)=='1'){
					sb1.append(str.charAt(idx));
				}else{
					sb2.append(str.charAt(idx));
				}
			}

			hash_arr = sb1.toString().toCharArray();
			Arrays.sort(hash_arr);
			s1 = new String(hash_arr);
			s2 = sb2.toString();

			if(dict_map.containsKey(dict_map.root,s1)){
				binary2 = getStringList(str.length()-n1,n2,stringMap);
				for(String t:binary2){
					sb3 = new StringBuilder();
					sb4 = new StringBuilder();
					for(int idx = 0;idx<t.length();idx++){
						if(t.charAt(idx)=='1'){
							sb3.append(s2.charAt(idx));
						}else{
							sb4.append(s2.charAt(idx));
						}
					}

					hash_arr = sb3.toString().toCharArray();
					Arrays.sort(hash_arr);
					s3 = new String(hash_arr);

					hash_arr = sb4.toString().toCharArray();
					Arrays.sort(hash_arr);
					s4 = new String(hash_arr);

					if(dict_map.containsKey(dict_map.root,s3) && dict_map.containsKey(dict_map.root,s4)){
						combine(s1,s3,s4,ans,dict_map);
					}
				}
			}
		}
	}

	public static void combine(String str1,String str2,AVLSet ans,AVLTree dict_map){
		ArrayList<String> l1 = dict_map.get(dict_map.root,str1);
		ArrayList<String> l2 = dict_map.get(dict_map.root,str2);

		for(String s1:l1){
			for(String s2:l2){
				ans.add(s1 + " " + s2);
				ans.add(s2 + " " + s1);
			}
		}
	}

	public static void combine(String str1,String str2,String str3,AVLSet ans,AVLTree dict_map){
		ArrayList<String> l1 = dict_map.get(dict_map.root,str1);
		ArrayList<String> l2 = dict_map.get(dict_map.root,str2);
		ArrayList<String> l3 = dict_map.get(dict_map.root,str3);

		for(String s1:l1){
			for(String s2:l2){
				for(String s3: l3){
					ans.add(s1 + " " + s2 + " " + s3);
					ans.add(s1 + " " + s3 + " " + s2);
					ans.add(s2 + " " + s1 + " " + s3);
					ans.add(s2 + " " + s3 + " " + s1);
					ans.add(s3 + " " + s2 + " " + s1);
					ans.add(s3 + " " + s1 + " " + s2);
				}
			}
		}
	}
}