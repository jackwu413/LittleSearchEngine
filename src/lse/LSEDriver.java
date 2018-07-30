package lse;

import java.util.ArrayList;
import java.io.FileNotFoundException;
//import java.util.HashMap;

public class LSEDriver {
	
	public static void main(String[] args) {
		
		LittleSearchEngine lse = new LittleSearchEngine();
		
		
//		ArrayList<Occurrence> list = new ArrayList<Occurrence>();
//		Occurrence one = new Occurrence ("test1.txt",12);
//		Occurrence two = new Occurrence ("test2.txt",8);
//		Occurrence three = new Occurrence ("test3.txt",7);
//		Occurrence four = new Occurrence ("test4.txt",5);
//		Occurrence five = new Occurrence ("test5.txt",3);
//		Occurrence six = new Occurrence ("test6.txt",2);
//		Occurrence seven = new Occurrence ("test7.txt",6);
//		list.add(one);
//		list.add(two);
//		list.add(three);
//		list.add(four);
//		list.add(five);
//		list.add(six);
//		list.add(seven);
//		ArrayList<Occurrence> occs = new ArrayList<Occurrence>();
//		Occurrence a = new Occurrence ("olivia", 38);
//		Occurrence b = new Occurrence ("olivia", 35);
//		Occurrence c = new Occurrence ("olivia", 31);
//		Occurrence d = new Occurrence ("olivia", 28);
//		Occurrence e = new Occurrence ("olivia", 22);
//		Occurrence f = new Occurrence ("olivia", 17);
//		Occurrence g = new Occurrence ("olivia", 12);
//		Occurrence h = new Occurrence ("olivia", 11);
//		Occurrence i = new Occurrence ("olivia", 9);
//		Occurrence j = new Occurrence ("olivia", 4);
//		Occurrence k = new Occurrence ("olivia", 3);
//		Occurrence l = new Occurrence ("olivia", 2);
//		Occurrence m = new Occurrence ("olivia", 1);
//		Occurrence n = new Occurrence ("olivia", 21);
//
//
//
//
//		occs.add(a);
//		occs.add(b);
//		occs.add(c);
//		occs.add(d);
//		occs.add(e);
//		occs.add(f);
//		occs.add(g);
//		occs.add(h);
//		occs.add(i);
//		occs.add(j);
//		occs.add(k);
//		occs.add(l);
//		occs.add(m);
//		occs.add(n);
//		
//		
//		lse.insertLastOccurrence(occs);
		
		try {
//			lse.makeIndex("docs.txt", "noisewords.txt");
			lse.getKeyword("sWord");
		} catch (FileNotFoundException e) {
			
		}
		
		for (String hi : lse.keywordsIndex.keySet())
			System.out.println(hi + " " + lse.keywordsIndex.get(hi));
		
		ArrayList<String> list = lse.top5search("say", "saw");
		System.out.println(list);
		

	}
}
