
PRIORITY (FIX THIS WEEKEND)
===

* Implement all measures. (DUE SATURDAY)
    * Implement rank biased sampling.
    * Is rank biased clusters ready?
    * Is rank biased overlap ready?

* Make entire simulator ready for runs. (DUE SUNDAY)
    * Run tests and see how it works. 
        * Does it break?
        * Is the stored files OK?
        * Go through each new file, write comments and explain logic.

Start simulations on MONDAY!






old
===
* I can greatly increase the speed through reusing retrieved lists with the same items.
    * I can at least have an inner loop for the expansion, where I reuse the same lists. 
    * Perhaps I can also to one poll with the most segmented version and then add together 
    the different segmentations, somewhat depending on how elastic has done it .
    * (It doesn't  what the "correct" list from elastic is, when I'm going to discuss RMs
     anyway.) 
* Implement expansion
* Normalise combiner - kolla overleaf (dubbelkolla och testkör)
* Scoring
  * create a module that does all tests, instead of running test with baseline at search time.
  * p must be a parameter, not hardcoded in the testing module.


perhaps
===
* Statistiska testet
   * utöka testet med RBO_min och RBO_max?
   
* Generating query terms
   * slumpa om artiklar som inte är på engelska
      * kinesiska tecken
      * japanska tecken
      * cyriliska tecken (bokstäver Ôóðýðð Èóóö ×óðúòø Ôóðýðð Blockinøöóðýøø×)
   * termen -evolution dök upp. få bort bindessträcket eller tillbaka vad som varit på andra sidan
   * -- and -  and AND happens in query terms
   * seed: 44, 46 (gave -null), 93 (lone -)
   
not priority / will probably not do at all
===

* förbättra sökmotorn
   * implementera den set-up som används i google scholar eller semantic scholar.

* refactoring
   * my use of pairs are unnecessary: i could just use jsonobjects with a comparator and maps in other cases

* testToInclueInCode
   * check for duplicates in final results early (should not be any though), otherwise the scoring will be rubbish

* new functionality
   * modul som skriver ut namnen på resultat sparade i filer

