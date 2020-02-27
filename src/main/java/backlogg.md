
* I can greatly increase the speed through reusing retrieved lists with the same items.
    * I can at least have an inner loop for the expansion, where I reuse the same lists. 
    * Perhaps I can also to one poll with the most segmented version and then add together 
    the different segmentations, somewhat depending on how elastic has done it .
    * (It doesn't  what the "correct" list from elastic is, when I'm going to discuss RMs
     anyway.) 

January
========

* Write citation expansion in theory

* Write method
   * Which experiments should I do?
      * How should setting be? Discuss all these parameters.
      * How do I combine subqueries and masterqueries?
         * How can I do many subqueries with same number of baseQueries?
   * How should the expansion be done conceptually
   * Statistiska testet
      * How can I use the statistical test between different simulations
      - Read up! If not clear after jan, ask Rebecka.

February
===

* Development
   * For-loop over settings
      * No baseSim method - use regular and send in settings
      * Only load queries once in looper
   * Implement expansion
   * Normalise combiner - kolla overleaf (dubbelkolla och testkör)
   * Scoring
      * create a module that does all tests, instead of running test with baseline at search time.
      * p must be a parameter, not hardcoded in the testing module.

* Write through report. STATUS OVERVIEW
   * How much of what I have is OK?
   * Create detailed backlogg
      * Which parts are not OK at all?

* Start with results section
   * Run simulations
   * Outline result
      * Which subsections?
      * Which figures?
         * create bar plot over means of many simulations
         * create 2d histogram of averaged scores for comparing two settings in same general case

Mars
===
* Run simulations

* R
   * Implement code to create figures to analyze

* Write result
    * Create figures
       
* Write discussion

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
   
* Optimising
   * profile code. what takes time?
   
not priority / will probably not do at all
===

* förbättra sökmotorn
   * implementera den set-up som används i google scholar eller semantic scholar.

* refactoring
   * my use of pairs are unnecessary: i could just use jsonobjects with a comparator and maps in other cases
   * general cleaning simulator
   * general cleaning simulator settings
   * rename features to modules

* testToInclueInCode
   * check for duplicates in final results early (should not be any though), otherwise the scoring will be rubbish

* new functionality
   * modul som skriver ut namnen på resultat sparade i filer

