# Anti-Spam
A spam filter implemented with Bayes Method.
Library
---
* **Mysql connection tool** connect database  via JDBC
	in Anti-Spa/lib
* [**jcseg**](https://code.google.com/p/jcseg/) segments Chinese words
* [**Tomcat**](http://tomcat.apache.org) on which this system runs

Bayes Method
---
* Bayes Method in Spam Detection :
 In this formula , S = "a spam", w = "a specific word showing in the mail" , N = "a normal email".
	><img src="http://latex.codecogs.com/gif.latex?P(S|w)&space;=&space;\frac{P(w|S)P(S)}{P(w|S)P(S)&space;&plus;&space;P(w|N)P(N)}" title="P(S|w) = \frac{P(w|S)P(S)}{P(w|S)P(S) + P(w|N)P(N)}" />

* An email certainly contains more than one word :
 ><img src="http://latex.codecogs.com/gif.latex?P(S|w_{i})&space;=&space;\frac{P(w_{i}|S)P(S)}{P(w_{i}|S)P(S)&space;&plus;&space;P(w_{i}|N)P(N)}" title="P(S|w_{i}) = \frac{P(w_{i}|S)P(S)}{P(w_{i}|S)P(S) + P(w_{i}|N)P(N)}" />
 
* Consider comprehensively, calculate combining probability :
><img src="http://latex.codecogs.com/gif.latex?P&space;=&space;\frac{P(S)\prod&space;P(S|w_{i})}{P(S)\prod&space;P(S|w_{i})&space;&plus;&space;(1&space;-&space;P(S))\prod(1&space;-&space;P(S|w_{i}))}" title="P = \frac{P(S)\prod P(S|w_{i})}{P(S)\prod P(S|w_{i}) + (1 - P(S))\prod(1 - P(S|w_{i}))}" />

*  Here we assume that <img src="http://latex.codecogs.com/gif.latex?S|w_{i}" title="S|w_{i}" /> are independent,so the formula above can be simplified as:
><img src="http://latex.codecogs.com/gif.latex?P&space;=&space;\frac{\prod&space;P(S|w_{i})}{\prod&space;P(S|w_{i})&space;&plus;&space;\prod&space;(1&space;-&space;P(S|w_{i}))}" title="P = \frac{\prod P(S|w_{i})}{\prod P(S|w_{i}) + \prod (1 - P(S|w_{i}))}" />	

* I'm using this formula to detect emails in this program.

Here are more [details](http://www.paulgraham.com/spam.html) of spam filtering, [fundamental knowledge](http://www.mathpages.com/home/kmath267.htm) of Combining Probability and [Bayes theorem](http://en.wikipedia.org/wiki/Bayes%27_theorem).