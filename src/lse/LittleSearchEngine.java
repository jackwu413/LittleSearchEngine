package lse;

import java.io.*;
import java.util.*;


/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {

	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		
		
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		sc = new Scanner(new File(docsFile));
		
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word){
		while (Character.isLetter(word.charAt(word.length()-1)) == false) {
			
			word = word.substring(0, word.length()-1);
			if (word.length()<1) {
				break;
			}
		}
		for (int i = 0; i < word.length(); i++) {
			if (Character.isLetter(word.charAt(i)) == false) {
				return null;
			}
		}
		word = word.toLowerCase();
	
		if (noiseWords.contains(word)) {
			return null;
		}
		return word;
	}
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		
		HashMap<String, Occurrence> list = new HashMap<String, Occurrence>();
		
		Scanner scanner = new Scanner (new File (docFile) );
		while (scanner.hasNext()) {
			String line = scanner.next();
			
			line = getKeyword(line);
			if (line != null) { 									//if the word is NOT a noise word
				if (list.containsKey(line)) { 					//if the final list already contains this word
					
					Occurrence temp = list.get(line);  			//get the existing occurrence of this word in the list 
					temp.frequency += 1;       					//update the frequency of this word

				} else {   										//if the list DOES NOT already contain this word
					Occurrence temp = new Occurrence(docFile,1); //make new occurrence for word starting with frequency 1
					list.put(line, temp); 						//put it in the list
				}
			} else {
				continue;
			}
		}
		scanner.close();
		return list;
	}
	
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		
		ArrayList<Integer> list = new ArrayList<Integer>(); 
		Occurrence last = occs.get(occs.size()-1);
		int value = occs.get(occs.size()-1).frequency;
		int min = 0;
		int max = occs.size()-2;
		while (min <= max) {
			int mid  = (min+max)/2;
			list.add(mid);
			
			if (value == occs.get(mid).frequency) {
				
				occs.remove(occs.size()-1);
				occs.add(mid, last);

				return list;
			} else if (value > occs.get(mid).frequency) {
				
				max = mid -1;
				
				if (max < min) {
					Occurrence val = occs.get(occs.size()-1);
					occs.remove(occs.size()-1);
					occs.add(mid, val);

					return list;
				}
				
				
			} else if (value < occs.get(mid).frequency) {
				
				min = mid + 1;
				
				if (max < min) {
					Occurrence val = occs.get(occs.size()-1);
					occs.remove(occs.size()-1);
					occs.add(mid+1, val);

					return list;
				}
				
			}
			
		}

		return list;
	}
	
	
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		
		for (Map.Entry<String, Occurrence> KWI : kws.entrySet()) {
			
			String key = KWI.getKey();
			Occurrence value = KWI.getValue();
			
			ArrayList<Occurrence> words = keywordsIndex.get(key);
			
			if (words != null) {
				words.add(value);
				insertLastOccurrence(words);
			} else {
				words = new ArrayList<Occurrence>();
				words.add(value);
				keywordsIndex.put(key, words);
			}
			
		}	
		
	}

	
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		
		if (keywordsIndex.containsKey(kw1) == false && keywordsIndex.containsKey(kw2) == false) {
			return null;
		}
		
		ArrayList<Occurrence> list1 = new ArrayList<Occurrence>();
		ArrayList<Occurrence> list2 = new ArrayList<Occurrence>();
		if (keywordsIndex.containsKey(kw1)) {
			list1 = keywordsIndex.get(kw1);
		}
		if (keywordsIndex.containsKey(kw2)) {
			list2 = keywordsIndex.get(kw2);
		}
		
		ArrayList<Occurrence> masterlist = new ArrayList<Occurrence>();
		ArrayList<String> doclist = new ArrayList<String>();
		
		while (list1.size() >= 1) {
			Occurrence temp1 = list1.get(list1.size()-1);
			list1.remove(list1.size()-1);
			masterlist.add(temp1);
			orderOccurrences(masterlist);
		}
		
		while (list2.size() >= 1) {
			Occurrence temp2 = list2.get(list2.size()-1);
			list2.remove(list2.size()-1);
			
			
			for (int i = 0; i < masterlist.size(); i++) {
				if (masterlist.get(i).document == temp2.document) {
					if (masterlist.get(i).frequency < temp2.frequency) {
						masterlist.remove(i);
						masterlist.add(temp2);
						break;
					} else if (masterlist.get(i).frequency > temp2.frequency) {
						break;
					}
				} else if (i == masterlist.size()-1) {
					masterlist.add(temp2);
					break;
				} else {
					continue;
				}
			}
			orderOccurrences(masterlist);
		}

		System.out.println("masterlist: " + masterlist);
		
		if (masterlist.size() >= 5) {
			while (doclist.size() <= 4) {
				doclist.add(masterlist.get(0).document);
				masterlist.remove(0);
			} 
		}
		
		else if (masterlist.size() < 5) {
			while (masterlist.size() >=1) {
				doclist.add(masterlist.get(0).document);
				masterlist.remove(0);
			}
		}
		System.out.println("SIZE OF doclist: " + doclist.size());
		System.out.println("This is the final top5: " + doclist);
		return doclist;
		
	}
	
	private ArrayList<Occurrence> orderOccurrences(ArrayList<Occurrence> occs) {
		
		
		Occurrence last = occs.get(occs.size()-1);
		int value = occs.get(occs.size()-1).frequency;
		int min = 0;
		int max = occs.size()-2;
		while (min <= max) {
			int mid  = (min+max)/2;
			
			
			if (value == occs.get(mid).frequency) {
				
				occs.remove(occs.size()-1);
				occs.add(mid, last);
				return occs;
			} else if (value > occs.get(mid).frequency) {
				
				max = mid -1;
				
				if (max < min) {
					Occurrence val = occs.get(occs.size()-1);
					occs.remove(occs.size()-1);
					occs.add(mid, val);
					return occs;
				}
				
				
			} else if (value < occs.get(mid).frequency) {
				
				min = mid + 1;
				
				if (max < min) {
					Occurrence val = occs.get(occs.size()-1);
					occs.remove(occs.size()-1);
					occs.add(mid+1, val);
					return occs;
				}
				
			}
			
		}	
		return occs;
	}
	
}



