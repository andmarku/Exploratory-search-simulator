
priority
========
* Simulation
   * I need to run for-loops over settings (started)
      * How can I do subqueries with same number of baseQueries?
   * Save loops in same file (without printing over)
   * (what combinations can I use? simulation set-up)

* Scoring
   * create a module that does all tests, instead of running test with baseline at search time.

* R
   * create bar plot over means of many simulations
   * create 2d histogram of averaged scores for comparing two settings in same general case

* Refactor
   * Run the statistical test in a separate unit. (Need to be able to rerun tests.)
      * p must be a parameter, not hardcoded in the testing module.
   * Rename runSimulation and SimulationTrialCase
   * Only load queries once in looper
   * Perhaps no baseSim method - use regular and send in settings
   
long term
=======
* Statistiska testet
   * se till så att jag kan använda det statistiska testet mellan flera olika simuleringar

* Features
   * fixa iordning expansion conceptuellt och sen implementationen
   * normalisera combiner - kolla conceptuellt på overleaf - typ färdigt, behöver bara dubbelkollas och testköras

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
   * seed: 44, 46 (gave -null), 93 (lone -)
   
* Optimising
   * profile code. what takes time?
   
* Refactoring
   * add folders
   
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

