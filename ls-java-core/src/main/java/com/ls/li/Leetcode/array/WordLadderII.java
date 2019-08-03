/**
 * 
 */
package com.ls.li.Leetcode.array;

import java.util.*;

/**
 * @author lishuai
 * @data 2016-12-1 下午5:34:05
 */

public class WordLadderII {

	/**
	 * @author lishuai
	 * @data 2016-12-1 下午5:34:05
Given two words (beginWord and endWord), and a dictionary's word list, 
find all shortest transformation sequence(s) from beginWord to endWord, such that:

Only one letter can be changed at a time
Each intermediate word must exist in the word list
For example,

Given:
beginWord = "hit"
endWord = "cog"
wordList = ["hot","dot","dog","lot","log"]
Return
  [
    ["hit","hot","dot","dog","cog"],
    ["hit","hot","lot","log","cog"]
  ]
Note:
All words have the same length.
All words contain only lowercase alphabetic characters.
	 */
	static Map<String,List<String>> map;
	static List<List<String>> results;
	public static void main(String[] args) {
		String s1 = "hit";
		String s2 = "cog";
		Set<String> s = new HashSet<String>();
		s.add("hot");
		s.add("dot");
		s.add("dog");
		s.add("lot");
		s.add("log");
		System.out.println(findLadders2(s1,s2,s));
	}
	//2
	public static List<List<String>> findLadders2(String start, String end,
            Set<String> dict) {
        List<List<String>> ladders = new ArrayList<List<String>>();
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        Map<String, Integer> distance = new HashMap<String, Integer>();

        dict.add(start);
        dict.add(end);
 
        bfs(map, distance, start, end, dict);
        
        List<String> path = new ArrayList<String>();
        
        dfs(ladders, path, end, start, distance, map);

        return ladders;
    }

	static void dfs(List<List<String>> ladders, List<String> path, String crt,
            String start, Map<String, Integer> distance,
            Map<String, List<String>> map) {
        path.add(crt);
        if (crt.equals(start)) {
            Collections.reverse(path);
            ladders.add(new ArrayList<String>(path));
            Collections.reverse(path);
        } else {
            for (String next : map.get(crt)) {
                if (distance.containsKey(next) && distance.get(crt) == distance.get(next) + 1) { 
                    dfs(ladders, path, next, start, distance, map);
                }
            }           
        }
        path.remove(path.size() - 1);
    }

	static void bfs(Map<String, List<String>> map, Map<String, Integer> distance,
            String start, String end, Set<String> dict) {
        Queue<String> q = new LinkedList<String>();
        q.offer(start);
        distance.put(start, 0);
        for (String s : dict) {
            map.put(s, new ArrayList<String>());
        }
        
        while (!q.isEmpty()) {
            String crt = q.poll();

            List<String> nextList = expand(crt, dict);
            for (String next : nextList) {
                map.get(next).add(crt);
                if (!distance.containsKey(next)) {
                    distance.put(next, distance.get(crt) + 1);
                    q.offer(next);
                }
            }
        }
    }

	static List<String> expand(String crt, Set<String> dict) {
        List<String> expansion = new ArrayList<String>();

        for (int i = 0; i < crt.length(); i++) {
            for (char ch = 'a'; ch <= 'z'; ch++) {
                if (ch != crt.charAt(i)) {
                    String expanded = crt.substring(0, i) + ch
                            + crt.substring(i + 1);
                    if (dict.contains(expanded)) {
                        expansion.add(expanded);
                    }
                }
            }
        }

        return expansion;
    }
	
	/**
The solution contains two steps 1 Use BFS to construct a graph. 
2. Use DFS to construct the paths from end to start.Both solutions got AC within 1s.

The first step BFS is quite important. I summarized three tricks

Using a MAP to store the min ladder of each word, or use a SET to store the words visited in current ladder, 
when the current ladder was completed, delete the visited words from unvisited. That's why I have two similar solutions.
Use Character iteration to find all possible paths. 
Do not compare one word to all the other words and check if they only differ by one character.
One word is allowed to be inserted into the queue only ONCE. See my comments.
	 */
	//1 有点问题
	public static List<List<String>> findLadders(String start, String end, Set<String> dict) {   	
        results= new ArrayList<List<String>>();
        if (dict.size() == 0)
			return results;
        
        int min=Integer.MAX_VALUE;
        
        Queue<String> queue= new ArrayDeque<String>();
        queue.add(start);
        
		map = new HashMap<String,List<String>>();
		
		Map<String,Integer> ladder = new HashMap<String,Integer>();
		for (String string:dict)
		    ladder.put(string, Integer.MAX_VALUE);
		ladder.put(start, 0);
				
		dict.add(end);
		//BFS: Dijisktra search
		while (!queue.isEmpty()) {
		   
			String word = queue.poll();
			
			int step = ladder.get(word)+1;//'step' indicates how many steps are needed to travel to one word. 
			
			if (step>min) break;
			
			for (int i = 0; i < word.length(); i++){
			   StringBuilder builder = new StringBuilder(word); 
				for (char ch='a';  ch <= 'z'; ch++){
					builder.setCharAt(i,ch);
					String new_word=builder.toString();				
					if (ladder.containsKey(new_word)) {
							
					    if (step>ladder.get(new_word))//Check if it is the shortest path to one word.
					    	continue;
					    else if (step<ladder.get(new_word)){
					    	queue.add(new_word);
					    	ladder.put(new_word, step);
					    }else;// It is a KEY line. If one word already appeared in one ladder,
					          // Do not insert the same word inside the queue twice. Otherwise it gets TLE.
					    
					    if (map.containsKey(new_word)) //Build adjacent Graph
					    	map.get(new_word).add(word);
					    else{
					    	List<String> list= new LinkedList<String>();
					    	list.add(word);
					    	map.put(new_word,list);
					    	//It is possible to write three lines in one:
					    	//map.put(new_word,new LinkedList<String>(Arrays.asList(new String[]{word})));
					    	//Which one is better?
					    }
					    
					    if (new_word.equals(end))
					    	min=step;

					}//End if dict contains new_word
				}//End:Iteration from 'a' to 'z'
			}//End:Iteration from the first to the last
		}//End While

    	//BackTracking
		LinkedList<String> result = new LinkedList<String>();
		backTrace(end,start,result);

		return results;        
    }
    private static void backTrace(String word,String start,List<String> list){
    	if (word.equals(start)){
    		list.add(0,start);
    		results.add(new ArrayList<String>(list));
    		list.remove(0);
    		return;
    	}
    	list.add(0,word);
    	if (map.get(word)!=null)
    		for (String s:map.get(word))
    			backTrace(s,start,list);
    	list.remove(0);
    }
	
    //0 
    public static List<List<String>> findLadders0(String beginWord, String endWord, Set<String> wordList) {
    	List<List<String>> res = new ArrayList<List<String>>();
    	
    	back(res, new ArrayList<String>(), beginWord, endWord, wordList, 0);
    	
    	
    	return res;   	
    }

    public static void back (List<List<String>> res,List<String> list,String beginWord, String endWord,Set<String> wordList,int start) {
    	if (beginWord.equals(endWord)) {
    		res.add(new ArrayList<String>(list));
    		return;
    	} 
    	for (int i = start;i < endWord.length();i++) {
    		if (wordList.contains(beginWord.charAt(i) + endWord.substring(i + 1))) {
    			list.add(beginWord);
    			beginWord = beginWord.charAt(i) + endWord.substring(i + 1);
    			back(res, list, beginWord, endWord, wordList, i + 1);   			
    		}
    		list.remove(list.size() - 1);
    	}
    }
    
}
