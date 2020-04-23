# Anagram-Generator-COL106

## Summary
Using hashing and AVL trees implemented anagram-generator which generates anagram of a given word from a large text collection.

## Goal
The goal of this assignment is to get some practice with collision resolution and hash functions. On the side you will also learn basic string manipulations. It’s a fun assignment where the task is to find valid anagrams of a given input.

## Problem Statement
You are given a vocabulary V of (lowercase) English words. It uses letters of English alphabet [a-z], digits [0-9], and the apostrophe symbol [']. No other characters are used in the vocabulary V. Your goal is to print out all valid anagrams of an input string. The input string will be a sequence of at most 12 characters.

### Anagram
Two strings are anagrams of each other if by rearranging letters of one string you can obtain the other. For example, “a bit” is an anagram of “bait”, and “super” is an anagram of “purse”. Note that we can add spaces at will, i.e., we won’t count spaces when matching characters for checking anagrams.<br/>

In this assignment, you will load V from the text file and then be ready to compute anagrams. You will be provided an input file also in the text format. In both vocabulary and input files there will be one string written per line. Your goal will be compute all valid anagrams (i.e., each word within your anagram must be present in V) of all input strings. After computing all valid anagrams of one string you must output a ‘-1’ to indicate that you are done computing anagrams of this string. For the purpose of this assignment, you only have to compute anagrams with a maximum of 2 spaces in them (i.e., three words at most). However, each permutation of these words will also be a valid anagram.<br/>

This is the first assignment in the course where you will be evaluated not only on the correctness of your code, but also on the runtime efficiency of the code. You can compute the time taken for your code to run using the built-in getTimeMillis() command.<br/>

## Vocabulary File
The vocabulary file (vocabulary.txt) will be provided in the resources of the assignment. The first line of the Vocabulary will indicate the number of words in the Vocabulary (V), followed by one word per line (all lowercase and no spaces). A sample vocabulary.txt is given below: <br/>
6<br/>
a<br/>
it<br/>
bit<br/>
bat<br/>
tab<br/>
i<br/>

## Input File
The input file (input.txt) will be an input to the code at runtime. The first line will have the number (K) of input strings. This will be followed by K lines, with one string per line. It will have only lowercase letters, digits, and apostrophe. It will not have a space. A sample input.txt is given as under<br/>
2<br/>
bait<br/>
bb<br/>

## Output File
You will produce all valid anagrams of each input string and output -1 after finishing with one input and moving onto the next. The output for a particular string should be in lexicographic order. For example, for the input file above you will output:<br/>
a bit<br/>
bat i<br/>
bit a<br/>
i bat<br/>
i tab<br/>
tab i<br/>
-1<br/>
-1<br/>

Note that for the second input word there were no valid anagrams found. Also note that the number of ‘-1’s in the output should be exactly same as the number of input words in input.txt. Your output must be produced on stdout (without any other extra information).


## Hashing
The main purpose of the assignment is to have you store vocabulary appropriately and have you check for anagrams efficiently. There may be many ways to store the vocabulary, but in this assignment you must hash each valid word. You will have to implement your own hash function and your own collision resolution. You may use chaining, or open addressing with any probe sequence. The goal is that your anagram computation should be as efficient as possible. You may use any function within Java built-in String class, except hashCode() or any other inbuilt hash functions.

## Tip
To compute better time efficiency not only will you have to implement a good hashing mechanism, you will also have to create an optimized approach to search through the space of anagrams. This may take some trial and error, so start early!
