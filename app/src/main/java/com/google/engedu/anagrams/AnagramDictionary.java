package com.google.engedu.anagrams;

import android.content.SyncAdapterType;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Arrays;
import java.util.Set;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private static ArrayList<String> wordList = new ArrayList<>();
    private static HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();
    private static Set<String> wordSet = new HashSet<String>();
    private static HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();
    private static int wordLength = DEFAULT_WORD_LENGTH;


    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sortWord = sortLetters(word);
            if(lettersToWord.get(sortWord) == null){
                lettersToWord.put(sortWord, new ArrayList<String>());
                lettersToWord.get(sortWord).add(word);
            }else{
                lettersToWord.get(sortWord).add(word);
            }
            if(sizeToWords.get(word.length()) == null){
                sizeToWords.put(word.length(), new ArrayList<String>());
                sizeToWords.get(word.length()).add(word);
            }else{
                sizeToWords.get(word.length()).add(word);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        boolean good = false;
        int start = 0;
        int end = base.length();
        String lookup;
        for(int i = base.length()-1; i < word.length(); i++){
            lookup = word.substring(start, end);
            System.out.println(lookup);
            if(sortLetters(lookup).equals(sortLetters(base))){
                if(lookup.equals(base)){
                    good = false;
                    break;
                }else{
                    good = wordSet.contains(lookup);
                    break;
                }
            }
            start++;
            end++;
        }
        return good;
    }

    public ArrayList<String> getAnagrams(String targetWord) {

        String sorted = sortLetters(targetWord);
        ArrayList<String> result = new ArrayList<String>();

        for(int i = 0; i < wordList.size(); i++){
            if(wordList.get(i).length() == sorted.length() && sortLetters(wordList.get(i)).equals(sorted)){
                result.add(wordList.get(i));
            }

        }



        return result;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        char[] letter = "abcdefghijklmnopqrstuvxyz".toCharArray();

        for(int i = 0; i < letter.length; i++){
            if(lettersToWord.containsKey(sortLetters(word + letter[i]))){
                ArrayList<String> list = lettersToWord.get(sortLetters(word + letter[i]));
                for(String anagrams : list){
                    result.add(anagrams);
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        int num = 0;
        ArrayList<String> list = sizeToWords.get(wordLength);
        while(num < MIN_NUM_ANAGRAMS) {
            String word = list.get((int)(Math.random()*list.size()));
            num = getAnagramsWithOneMoreLetter(word).size();
            if (num >= MIN_NUM_ANAGRAMS) {
                if(wordLength < MAX_WORD_LENGTH)
                    wordLength++;
                return word;
            }
        }
        return "stop";
    }

    public String sortLetters(String word) {
        String unsorted = word;
        char[] chars = unsorted.toCharArray();
        Arrays.sort(chars);
        String sorted = new String(chars);

        return sorted;
    }
}
