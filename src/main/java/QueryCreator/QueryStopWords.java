package QueryCreator;

public class QueryStopWords {

    // amount bill bottom describe detail empty fill find for front full interest keep mill move name part put seem seems side system take thick thin through while
    // from page http://xpo6.com/list-of-english-stop-words/
    private static final String stopWordsFromWeb = "a"+ "|" + "about"+ "|" + "above"+ "|" + "above"+ "|" + "across"+ "|" +
            "after"+ "|" + "afterwards"+ "|" + "again"+ "|" + "against"+ "|" + "all"+ "|" + "almost"+ "|" +
            "alone"+ "|" + "along"+ "|" + "already"+ "|" + "also"+ "|" +"although"+ "|" +"always"+ "|" +"am"+ "|" +
            "among"+ "|" + "amongst"+ "|" + "amoungst"+ "|" + "amount"+ "|" +  "an"+ "|" + "and"+ "|" +
            "another"+ "|" + "any"+ "|" +"anyhow"+ "|" +"anyone"+ "|" +"anything"+ "|" +"anyway"+ "|" +
            "anywhere"+ "|" + "are"+ "|" + "around"+ "|" + "as"+ "|" +  "at"+ "|" + "back"+ "|" +"be"+ "|" +
            "became"+ "|" + "because"+ "|" +"become"+ "|" +"becomes"+ "|" + "becoming"+ "|" + "been"+ "|" +
            "before"+ "|" + "beforehand"+ "|" + "behind"+ "|" + "being"+ "|" + "below"+ "|" + "beside"+ "|" +
            "besides"+ "|" + "between"+ "|" + "beyond"+ "|" + "bill"+ "|" + "both"+ "|" + "bottom"+ "|" +
            "but"+ "|" + "by"+ "|" + "call"+ "|" + "can"+ "|" + "cannot"+ "|" + "cant"+ "|" + "co"+ "|" +
            "con"+ "|" + "could"+ "|" + "couldnt"+ "|" + "cry"+ "|" + "de"+ "|" + "describe"+ "|" + "detail"+ "|" +
            "do"+ "|" + "done"+ "|" + "down"+ "|" + "due"+ "|" + "during"+ "|" + "each"+ "|" + "eg"+ "|" +
            "eight"+ "|" + "either"+ "|" + "eleven"+ "|" +"else"+ "|" + "elsewhere"+ "|" + "empty"+ "|" +
            "enough"+ "|" + "etc"+ "|" + "even"+ "|" + "ever"+ "|" + "every"+ "|" + "everyone"+ "|" +
            "everything"+ "|" + "everywhere"+ "|" + "except"+ "|" + "few"+ "|" + "fifteen"+ "|" + "fifty"+ "|" +
            "fill"+ "|" + "find"+ "|" + "fire"+ "|" + "first"+ "|" + "five"+ "|" + "for"+ "|" + "former"+ "|" +
            "formerly"+ "|" + "forty"+ "|" + "found"+ "|" + "four"+ "|" + "from"+ "|" + "front"+ "|" +
            "full"+ "|" + "further"+ "|" + "get"+ "|" + "give"+ "|" + "go"+ "|" + "had"+ "|" + "has"+ "|" +
            "hasnt"+ "|" + "have"+ "|" + "he"+ "|" + "hence"+ "|" + "her"+ "|" + "here"+ "|" + "hereafter"+ "|" +
            "hereby"+ "|" + "herein"+ "|" + "hereupon"+ "|" + "hers"+ "|" + "herself"+ "|" + "him"+ "|" +
            "himself"+ "|" + "his"+ "|" + "how"+ "|" + "however"+ "|" + "hundred"+ "|" + "ie"+ "|" + "if"+ "|" +
            "in"+ "|" + "inc"+ "|" + "indeed"+ "|" + "interest"+ "|" + "into"+ "|" + "is"+ "|" + "it"+ "|" +
            "its"+ "|" + "itself"+ "|" + "keep"+ "|" + "last"+ "|" + "latter"+ "|" + "latterly"+ "|" + "least"+ "|" +
            "less"+ "|" + "ltd"+ "|" + "made"+ "|" + "many"+ "|" + "may"+ "|" + "me"+ "|" + "meanwhile"+ "|" +
            "might"+ "|" + "mill"+ "|" + "mine"+ "|" + "more"+ "|" + "moreover"+ "|" + "most"+ "|" + "mostly"+ "|" +
            "move"+ "|" + "much"+ "|" + "must"+ "|" + "my"+ "|" + "myself"+ "|" + "name"+ "|" + "namely"+ "|" +
            "neither"+ "|" + "never"+ "|" + "nevertheless"+ "|" + "next"+ "|" + "nine"+ "|" + "no"+ "|" + "nobody"+ "|" +
            "none"+ "|" + "noone"+ "|" + "nor"+ "|" + "not"+ "|" + "nothing"+ "|" + "now"+ "|" + "nowhere"+ "|" +
            "of"+ "|" + "off"+ "|" + "often"+ "|" + "on"+ "|" + "once"+ "|" + "one"+ "|" + "only"+ "|" + "onto"+ "|" +
            "or"+ "|" + "other"+ "|" + "others"+ "|" + "otherwise"+ "|" + "our"+ "|" + "ours"+ "|" + "ourselves"+ "|" +
            "out"+ "|" + "over"+ "|" + "own"+ "|" +"part"+ "|" + "per"+ "|" + "perhaps"+ "|" + "please"+ "|" +
            "put"+ "|" + "rather"+ "|" + "re"+ "|" + "same"+ "|" + "see"+ "|" + "seem"+ "|" + "seemed"+ "|" +
            "seeming"+ "|" + "seems"+ "|" + "serious"+ "|" + "several"+ "|" + "she"+ "|" + "should"+ "|" +
            "show"+ "|" + "side"+ "|" + "since"+ "|" + "sincere"+ "|" + "six"+ "|" + "sixty"+ "|" + "so"+ "|" +
            "some"+ "|" + "somehow"+ "|" + "someone"+ "|" + "something"+ "|" + "sometime"+ "|" + "sometimes"+ "|" +
            "somewhere"+ "|" + "still"+ "|" + "such"+ "|" + "system"+ "|" + "take"+ "|" + "ten"+ "|" + "than"+ "|" +
            "that"+ "|" + "the"+ "|" + "their"+ "|" + "them"+ "|" + "themselves"+ "|" + "then"+ "|" + "thence"+ "|" +
            "there"+ "|" + "thereafter"+ "|" + "thereby"+ "|" + "therefore"+ "|" + "therein"+ "|" + "thereupon"+ "|" +
            "these"+ "|" + "they"+ "|" + "thick"+ "|" + "thin"+ "|" + "third"+ "|" + "this"+ "|" + "those"+ "|" +
            "though"+ "|" + "three"+ "|" + "through"+ "|" + "throughout"+ "|" + "thru"+ "|" + "thus"+ "|" + "to"+ "|" +
            "together"+ "|" + "too"+ "|" + "top"+ "|" + "toward"+ "|" + "towards"+ "|" + "twelve"+ "|" + "twenty"+ "|" +
            "two"+ "|" + "un"+ "|" + "under"+ "|" + "until"+ "|" + "up"+ "|" + "upon"+ "|" + "us"+ "|" + "very"+ "|" +
            "via"+ "|" + "was"+ "|" + "we"+ "|" + "well"+ "|" + "were"+ "|" + "what"+ "|" + "whatever"+ "|" +
            "when"+ "|" + "whence"+ "|" + "whenever"+ "|" + "where"+ "|" + "whereafter"+ "|" + "whereas"+ "|" +
            "whereby"+ "|" + "wherein"+ "|" + "whereupon"+ "|" + "wherever"+ "|" + "whether"+ "|" + "which"+ "|" +
            "while"+ "|" + "whither"+ "|" + "who"+ "|" + "whoever"+ "|" + "whole"+ "|" + "whom"+ "|" + "whose"+ "|" +
            "why"+ "|" + "will"+ "|" + "with"+ "|" + "within"+ "|" + "without"+ "|" + "would"+ "|" + "yet"+ "|" +
            "you"+ "|" + "your"+ "|" + "yours"+ "|" + "yourself"+ "|" + "yourselves"+ "|" + "the";

    private static final String STOPWORDS_CAPITAL =
            "A"+ "|" + "About"+ "|" + "Above"+ "|" + "Above"+ "|" + "Across"+ "|" +
            "After"+ "|" + "Afterwards"+ "|" + "Again"+ "|" + "Against"+ "|" + "All"+ "|" + "Almost"+ "|" +
            "Alone"+ "|" + "Along"+ "|" + "Already"+ "|" + "Also"+ "|" +"Although"+ "|" +"Always"+ "|" +"Am"+ "|" +
            "Among"+ "|" + "Amongst"+ "|" + "Amoungst"+ "|" + "Amount"+ "|" +  "An"+ "|" + "And"+ "|" +
            "Another"+ "|" + "Any"+ "|" +"Anyhow"+ "|" +"Anyone"+ "|" +"Anything"+ "|" +"Anyway"+ "|" +
            "Anywhere"+ "|" + "Are"+ "|" + "Around"+ "|" + "As"+ "|" +  "At"+ "|" + "Back"+ "|" +"Be"+ "|" +
            "Became"+ "|" + "Because"+ "|" +"Become"+ "|" +"Becomes"+ "|" + "Becoming"+ "|" + "Been"+ "|" +
            "Before"+ "|" + "Beforehand"+ "|" + "Behind"+ "|" + "Being"+ "|" + "Below"+ "|" + "Beside"+ "|" +
            "Besides"+ "|" + "Between"+ "|" + "Beyond"+ "|" + "Bill"+ "|" + "Both"+ "|" + "Bottom"+ "|" +
            "But"+ "|" + "By"+ "|" + "Call"+ "|" + "Can"+ "|" + "Cannot"+ "|" + "Cant"+ "|" + "Co"+ "|" +
            "Con"+ "|" + "Could"+ "|" + "Couldnt"+ "|" + "Cry"+ "|" + "De"+ "|" + "Describe"+ "|" + "Detail"+ "|" +
            "Do"+ "|" + "Done"+ "|" + "Down"+ "|" + "Due"+ "|" + "During"+ "|" + "Each"+ "|" + "Eg"+ "|" +
            "Eight"+ "|" + "Either"+ "|" + "Eleven"+ "|" +"Else"+ "|" + "Elsewhere"+ "|" + "Empty"+ "|" +
            "Enough"+ "|" + "Etc"+ "|" + "Even"+ "|" + "Ever"+ "|" + "Every"+ "|" + "Everyone"+ "|" +
            "Everything"+ "|" + "Everywhere"+ "|" + "Except"+ "|" + "Few"+ "|" + "Fifteen"+ "|" + "Fify"+ "|" +
            "Fill"+ "|" + "Find"+ "|" + "Fire"+ "|" + "First"+ "|" + "Five"+ "|" + "For"+ "|" + "Former"+ "|" +
            "Formerly"+ "|" + "Forty"+ "|" + "Found"+ "|" + "Four"+ "|" + "From"+ "|" + "Front"+ "|" +
            "Full"+ "|" + "Further"+ "|" + "Get"+ "|" + "Give"+ "|" + "Go"+ "|" + "Had"+ "|" + "Has"+ "|" +
            "Hasnt"+ "|" + "Have"+ "|" + "He"+ "|" + "Hence"+ "|" + "Her"+ "|" + "Here"+ "|" + "Hereafter"+ "|" +
            "Hereby"+ "|" + "Herein"+ "|" + "Hereupon"+ "|" + "Hers"+ "|" + "Herself"+ "|" + "Him"+ "|" +
            "Himself"+ "|" + "His"+ "|" + "How"+ "|" + "However"+ "|" + "Hundred"+ "|" + "Ie"+ "|" + "If"+ "|" +
            "In"+ "|" + "Inc"+ "|" + "Indeed"+ "|" + "Interest"+ "|" + "Into"+ "|" + "Is"+ "|" + "It"+ "|" +
            "Its"+ "|" + "Itself"+ "|" + "Keep"+ "|" + "Last"+ "|" + "Latter"+ "|" + "Latterly"+ "|" + "Least"+ "|" +
            "Less"+ "|" + "Made"+ "|" + "Many"+ "|" + "May"+ "|" + "Me"+ "|" + "Meanwhile"+ "|" +
            "Might"+ "|" + "Mill"+ "|" + "Mine"+ "|" + "More"+ "|" + "Moreover"+ "|" + "Most"+ "|" + "Mostly"+ "|" +
            "Move"+ "|" + "Much"+ "|" + "Must"+ "|" + "My"+ "|" + "Myself"+ "|" + "Name"+ "|" + "Namely"+ "|" +
            "Neither"+ "|" + "Never"+ "|" + "Nevertheless"+ "|" + "Next"+ "|" + "Nine"+ "|" + "No"+ "|" + "Nobody"+ "|" +
            "None"+ "|" + "Noone"+ "|" + "Nor"+ "|" + "Not"+ "|" + "Nothing"+ "|" + "Now"+ "|" + "Nowhere"+ "|" +
            "Of"+ "|" + "Off"+ "|" + "Often"+ "|" + "On"+ "|" + "Once"+ "|" + "One"+ "|" + "Only"+ "|" + "Onto"+ "|" +
            "Or"+ "|" + "Other"+ "|" + "Others"+ "|" + "Otherwise"+ "|" + "Our"+ "|" + "Ours"+ "|" + "Ourselves"+ "|" +
            "Out"+ "|" + "Over"+ "|" + "Own"+ "|" +"Part"+ "|" + "Per"+ "|" + "Perhaps"+ "|" + "Please"+ "|" +
            "Put"+ "|" + "Rather"+ "|" + "Re"+ "|" + "Same"+ "|" + "See"+ "|" + "Seem"+ "|" + "Seemed"+ "|" +
            "Seeming"+ "|" + "Seems"+ "|" + "Serious"+ "|" + "Several"+ "|" + "She"+ "|" + "Should"+ "|" +
            "Show"+ "|" + "Side"+ "|" + "Since"+ "|" + "Sincere"+ "|" + "Six"+ "|" + "Sixty"+ "|" + "So"+ "|" +
            "Some"+ "|" + "Somehow"+ "|" + "Someone"+ "|" + "Something"+ "|" + "Sometime"+ "|" + "Sometimes"+ "|" +
            "Somewhere"+ "|" + "Still"+ "|" + "Such"+ "|" + "System"+ "|" + "Take"+ "|" + "Ten"+ "|" + "Than"+ "|" +
            "That"+ "|" + "The"+ "|" + "Their"+ "|" + "Them"+ "|" + "Themselves"+ "|" + "Then"+ "|" + "Thence"+ "|" +
            "There"+ "|" + "Thereafter"+ "|" + "Thereby"+ "|" + "Therefore"+ "|" + "Therein"+ "|" + "Thereupon"+ "|" +
            "These"+ "|" + "They"+ "|" + "Thick"+ "|" + "Thin"+ "|" + "Third"+ "|" + "This"+ "|" + "Those"+ "|" +
            "Though"+ "|" + "Three"+ "|" + "Through"+ "|" + "Throughout"+ "|" + "Thru"+ "|" + "Thus"+ "|" + "To"+ "|" +
            "Together"+ "|" + "Too"+ "|" + "Top"+ "|" + "Toward"+ "|" + "Towards"+ "|" + "Twelve"+ "|" + "Twenty"+ "|" +
            "Two"+ "|" + "Un"+ "|" + "Under"+ "|" + "Until"+ "|" + "Up"+ "|" + "Upon"+ "|" + "Us"+ "|" + "Very"+ "|" +
            "Via"+ "|" + "Was"+ "|" + "We"+ "|" + "Well"+ "|" + "Were"+ "|" + "What"+ "|" + "Whatever"+ "|" +
            "When"+ "|" + "Whence"+ "|" + "Whenever"+ "|" + "Where"+ "|" + "Whereafter"+ "|" + "Whereas"+ "|" +
            "Whereby"+ "|" + "Wherein"+ "|" + "Whereupon"+ "|" + "Wherever"+ "|" + "Whether"+ "|" + "Which"+ "|" +
            "While"+ "|" + "Whither"+ "|" + "Who"+ "|" + "Whoever"+ "|" + "Whole"+ "|" + "Whom"+ "|" + "Whose"+ "|" +
            "Why"+ "|" + "Will"+ "|" + "With"+ "|" + "Within"+ "|" + "Without"+ "|" + "Would"+ "|" + "Yet"+ "|" +
            "You"+ "|" + "Your"+ "|" + "Yours"+ "|" + "Yourself"+ "|" + "Yourselves"+ "|" + "The";
    
    private static final String STOPWORDS_ENGLISH = stopWordsFromWeb  + "|"  + STOPWORDS_CAPITAL +
            "|" + "vs" + "|"  + "it's" + "|"  +  "It's" + "|"  + "hasn't" + "|" + "Hasn't"+
            "|" + "can't" + "|" + "Can't" + "|" + "couldn't" + "|" + "Couldn't";
    
    private static final String STOPWORDS_FRENCH_LOWER = "à" + "|" + "l'" + "|" + "les" + "|" + "qui" + "|" + "d'" + "|" + "t'" + "|" +
            "je" + "|" + "de" + "|" + "ne" + "|" + "le" + "|" + "la" + "|" + "tu" + "|" + "vous" + "|" + "il" + "|" +
            "et" + "|" + "un" + "|" + "en" + "|" + "ça" + "|" + "ce" + "|" + "une" + "|" + "se" + "|" + "des" + "|" +
            "elle" + "|" + "du" + "|" + "y" + "|" + "non" + "|" + "te" + "|" + "mon";

    private static final String STOPWORDS_FRENCH_CAPITAL = "À" + "|" + "L'" + "|" + "Les" + "|" + "Qui" + "|" + "D'" + "|" + "T'" + "|" +
            "Je" + "|" + "De" + "|" + "Ne" + "|" + "Le" + "|" + "La" + "|" + "Tu" + "|" + "Vous" + "|" + "Il" + "|" +
            "Et" + "|" + "Un" + "|" + "En" + "|" + "Ce" + "|" + "Une" + "|" + "Se" + "|" + "Des" + "|" +
            "Elle" + "|" + "Du" + "|" + "Y" + "|" + "Non" + "|" + "Te" + "|" + "Mon";

    private static final String STOPWORDS_SPECIAL_SIGNS = "\\s-{1,2}" + "|" + "-{1,2}\\s";

    public static final String STOPWORDS = STOPWORDS_ENGLISH + "|" + STOPWORDS_FRENCH_LOWER +
            "|" + STOPWORDS_FRENCH_CAPITAL + "|" +  STOPWORDS_SPECIAL_SIGNS;

    public static final String SIGNS = "," + ":" + "\\." + ";" + "\\[" + "\\]" + "\\(" + "\\)" + "\"" + "\'" + "?";
}
