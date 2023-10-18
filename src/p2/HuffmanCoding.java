package p2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import p2.DataStructures.List.List;
import p2.DataStructures.Map.HashTableSC;
import p2.DataStructures.Map.Map;
import p2.DataStructures.SortedList.SortedLinkedList;
import p2.DataStructures.SortedList.SortedList;
import p2.DataStructures.Tree.BTNode;
import p2.Utils.BinaryTreePrinter;

/**
 * The Huffman Encoding Algorithm
 * 
 * This is a data compression algorithm designed 
 * by David A. Huffman and published in 1952
 * 
 * What it does is it takes a string and by constructing 
 * a special binary tree with the frequencies of each character.
 * 
 * This tree generates special prefix codes that make the size 
 * of each string encoded a lot smaller, thus saving space.
 * 
 * @author Fernando J. Bermudez Medina (Template) 
 * @author Lucas Maldonado Montanez - 841 18 0561 (Implementation)
 * @version 3.0
 * @since 03/28/2023
 */
public class HuffmanCoding {

	public static void main(String[] args) {
		HuffmanEncodedResult();
	}

	/* This method just runs all the main methods developed or the algorithm */
	private static void HuffmanEncodedResult() {
		/* You can create other test input files and add them to the inputData Folder */
		String data = load_data("input1.txt");

		/* If input string is not empty we can encode the text using our algorithm */
		if(!data.isEmpty()) {
			Map<String, Integer> fD = compute_fd(data);
			BTNode<Integer,String> huffmanRoot = huffman_tree(fD);
			Map<String,String> encodedHuffman = huffman_code(huffmanRoot);
			String output = encode(encodedHuffman, data);
			process_results(fD, encodedHuffman,data,output);
		} else 
			System.out.println("Input Data Is Empty! Try Again with a File that has data inside!");
		

	}

	/**
	 * Receives a file named in parameter inputFile (including its path),
	 * and returns a single string with the contents.
	 * 
	 * @param inputFile name of the file to be processed in the path inputData/
	 * @return String with the information to be processed
	 */
	public static String load_data(String inputFile) {
		BufferedReader in = null;
		String line = "";

		try {
			/**
			 * We create a new reader that accepts UTF-8 encoding and 
			 * extract the input string from the file, and we return it
			 */
			in = new BufferedReader(new InputStreamReader(new FileInputStream("inputData/" + inputFile), "UTF-8"));

			/**
			 * If input file is empty just return an 
			 * empty string, if not just extract the data
			 */
			String extracted = in.readLine();
			if(extracted != null)
				line = extracted;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) 
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return line;
	}
	
	 /*
	 * compute_fd method calculates the frequency distribution of characters in the input string.
	 * It takes the input string and iterates through it, incrementing the count of each character
	 * in a frequency distribution map. 
	 * 
	 * @param inputString the string to compute the frequency distribution for
	 * @return Map containing the characters and their frequencies
	 */
	
	public static Map<String, Integer> compute_fd(String inputString) {
	    Map<String, Integer> charFrequencyMap = new HashTableSC<>();

	    int inputLength = inputString.length();
	    int index = 0;
	    while (index < inputLength) {
	        String currentChar = String.valueOf(inputString.charAt(index));
	        Integer currentCount = charFrequencyMap.get(currentChar);

	        if (currentCount == null) {
	            charFrequencyMap.put(currentChar, 1);
	        } else {
	            charFrequencyMap.put(currentChar, currentCount + 1);
	        }
	        index++;
	    }

	    return charFrequencyMap;
	}

	 /*
	 * huffman_tree method constructs the Huffman tree based on the given frequency distribution map.
	 * It creates a sorted list of nodes representing characters and their frequencies, then builds
	 * the tree by iteratively combining the two nodes with the smallest frequencies until only one node remains.
	 * 
	 * @param fD The frequency distribution map for the input string
	 * @return The root node of the Huffman tree
	 */
	
	public static BTNode<Integer, String> huffman_tree(Map<String, Integer> fD) {
	    SortedList<BTNode<Integer, String>> sortedList = new SortedLinkedList<>();

	    for (String key : fD.getKeys()) {
	        BTNode<Integer, String> node = new BTNode<>(fD.get(key), key);
	        sortedList.add(node);
	    }

	    return buildHuffmanTreeRecursively(sortedList);
	}
	
	/*
	* This method is a helper function of the huffman_tree method that recursively constructs the Huffman tree
	* based on the sorted list of nodes representing characters and their frequencies. It combines the two nodes
	* with the smallest frequencies to create a parent node and adds it to the sorted list until only one node remains.
	* @param sortedList A sorted list of nodes representing characters and their frequencies
	* @return The root node of the Huffman tree
	*/

	private static BTNode<Integer, String> buildHuffmanTreeRecursively(SortedList<BTNode<Integer, String>> sortedList) {
	    if (sortedList.size() == 1) {
	        System.out.println("\n\n");
	        BinaryTreePrinter.print(sortedList.get(0));
	        System.out.println("\n\n");
	        return sortedList.get(0);
	    }

	    if (sortedList.size() < 2) {
	        // Handle the case when there are not enough nodes in the list
	        return null;
	    }

	    BTNode<Integer, String> leftNode = sortedList.removeIndex(0);
	    BTNode<Integer, String> rightNode = sortedList.removeIndex(0);

	    BTNode<Integer, String> parentNode = new BTNode<>(leftNode.getKey() + rightNode.getKey(), leftNode.getValue() + rightNode.getValue());
	    parentNode.setLeftChild(leftNode);
	    parentNode.setRightChild(rightNode);

	    sortedList.add(parentNode);

	    return buildHuffmanTreeRecursively(sortedList);
	}

	 /*
	 * huffman_code method generates the Huffman prefix codes for each character in the Huffman tree.
	 * It traverses the tree and assigns a binary code to each character based on the path from the root node.
	 * 
	 * @param huffmanRoot The root node of the Huffman tree
	 * @return A Map of character-Huffman code pairs, where the key is a character and the value is its Huffman code
	 */
	
	public static Map<String, String> huffman_code(BTNode<Integer, String> huffmanRoot) {
	    Map<String, String> huffmanCodes = new HashTableSC<String, String>();
	    generateHuffmanCodes(huffmanRoot, huffmanCodes, "");
	    return huffmanCodes;
	}

	private static void generateHuffmanCodes(BTNode<Integer, String> currentNode, Map<String, String> huffmanCodes, String code) {
	    if (currentNode == null) {
	        return;
	    }

	    if (currentNode.getLeftChild() == null && currentNode.getRightChild() == null) {
	        huffmanCodes.put(currentNode.getValue(), code);
	    }

	    generateHuffmanCodes(currentNode.getLeftChild(), huffmanCodes, code + "0");
	    generateHuffmanCodes(currentNode.getRightChild(), huffmanCodes, code + "1");
	}

	 /*
	 * encode method takes the encoding map and input string, and encodes the input string using the provided encoding map.
	 * It iterates through the input string, replacing each character with its corresponding Huffman code.
	 */
	
	public static String encode(Map<String, String> codeMap, String originalString) {
	    StringBuilder encodedString = new StringBuilder();
	    for (char character : originalString.toCharArray()) {
	        String encodedCharacter = codeMap.get(String.valueOf(character));
	        encodedString.append(encodedCharacter);
	    }
	    return encodedString.toString();
	}

	/**
	 * Receives the frequency distribution map, the Huffman Prefix Code HashTable, the input string, 
	 * and the output string, and prints the results to the screen (per specifications).
	 * 
	 * Output Includes: symbol, frequency and code. 
	 * Also includes how many bits has the original and encoded string, plus how much space was saved using this encoding algorithm
	 * 
	 * @param fD Frequency Distribution of all the characters in input string
	 * @param encodedHuffman Prefix Code Map
	 * @param inputData text string from the input file
	 * @param output processed encoded string
	 */
	
	public static void process_results(Map<String, Integer> fD, Map<String, String> encodedHuffman, String inputData, String output) {
		/*To get the bytes of the input string, we just get the bytes of the original string with string.getBytes().length*/
		int inputBytes = inputData.getBytes().length;

		/**
		 * For the bytes of the encoded one, it's not so easy.
		 * 
		 * Here we have to get the bytes the same way we got the bytes 
		 * for the original one but we divide it by 8, because 
		 * 1 byte = 8 bits and our huffman code is in bits (0,1), not bytes. 
		 * 
		 * This is because we want to calculate how many bytes we saved by 
		 * counting how many bits we generated with the encoding 
		 */
		DecimalFormat d = new DecimalFormat("##.##");
		double outputBytes = Math.ceil((float) output.getBytes().length / 8);

		/**
		 * to calculate how much space we saved we just take the percentage.
		 * the number of encoded bytes divided by the number of original bytes 
		 * will give us how much space we "chopped off".
		 * 
		 * So we have to subtract that "chopped off" percentage to the total (which is 100%) 
		 * and that's the difference in space required
		 */
		String savings =  d.format(100 - (( (float) (outputBytes / (float)inputBytes) ) * 100));


		/**
		 * Finally we just output our results to the console 
		 * with a more visual pleasing version of both our 
		 * Hash Tables in decreasing order by frequency.
		 * 
		 * Notice that when the output is shown, the characters 
		 * with the highest frequency have the lowest amount of bits.
		 * 
		 * This means the encoding worked and we saved space!
		 */
		System.out.println("Symbol\t" + "Frequency   " + "Code");
		System.out.println("------\t" + "---------   " + "----");

		SortedList<BTNode<Integer,String>> sortedList = new SortedLinkedList<BTNode<Integer,String>>();

		/**
		 * To print the table in decreasing order by frequency, 
		 * we do the same thing we did when we built the tree.
		 * 
		 * We add each key with it's frequency in a node into a SortedList, 
		 * this way we get the frequencies in ascending order
		 */
		for (String key : fD.getKeys()) {
			BTNode<Integer,String> node = new BTNode<Integer,String>(fD.get(key),key);
			sortedList.add(node);
		}

		/**
		 * Since we have the frequencies in ascending order, 
		 * we just traverse the list backwards and start printing 
		 * the nodes key (character) and value (frequency) and find 
		 * the same key in our prefix code "Lookup Table" we made 
		 * earlier on in huffman_code(). 
		 * 
		 * That way we get the table in decreasing order by frequency
		 */
		for (int i = sortedList.size() - 1; i >= 0; i--) {
			BTNode<Integer,String> node = sortedList.get(i);
			System.out.println(node.getValue() + "\t" + node.getKey() + "\t    " + encodedHuffman.get(node.getValue()));
		}

		System.out.println("\nOriginal String: \n" + inputData);
		System.out.println("Encoded String: \n" + output);
		System.out.println("Decoded String: \n" + decodeHuff(output, encodedHuffman) + "\n");
		System.out.println("The original string requires " + inputBytes + " bytes.");
		System.out.println("The encoded string requires " + (int) outputBytes + " bytes.");
		System.out.println("Difference in space requiered is " + savings + "%.");
	}


	/*************************************************************************************
	 ** ADD ANY AUXILIARY METHOD YOU WISH TO IMPLEMENT TO FACILITATE YOUR SOLUTION HERE **
	 *************************************************************************************/

	/**
	 * Auxiliary Method that decodes the generated string by the Huffman Coding Algorithm
	 * 
	 * Used for output Purposes
	 * 
	 * @param output - Encoded String
	 * @param lookupTable 
	 * @return The decoded String, this should be the original input string parsed from the input file
	 */
	public static String decodeHuff(String output, Map<String, String> lookupTable) {
		String result = "";
		int start = 0;
		List<String>  prefixCodes = lookupTable.getValues();
		List<String> symbols = lookupTable.getKeys();

		/**
		 * Loop through output until a prefix code is found on map and 
		 * adding the symbol that the code that represents it to result 
		 */
		for(int i = 0; i <= output.length();i++){

			String searched = output.substring(start, i);

			int index = prefixCodes.firstIndex(searched);

			if(index >= 0) { // Found it!
				result= result + symbols.get(index);
				start = i;
			}
		}
		return result;    
	}
}