/**
 * 
 */
package com.shtick.apps.sh.qagen.math.fourth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import com.shtick.apps.sh.core.Driver;
import com.shtick.apps.sh.core.Question;
import com.shtick.apps.sh.core.Subject;
import com.shtick.apps.sh.core.SubjectQuestionGenerator;
import com.shtick.apps.sh.core.UserID;
import com.shtick.apps.sh.core.content.Choice;
import com.shtick.apps.sh.core.content.Marshal;
import com.shtick.apps.sh.core.content.MultipleChoice;
import com.shtick.apps.sh.qagen.math.fourth.Noun.Case;
import com.shtick.utils.data.json.JSONEncoder;

/**
 * @author sean.cox
 *
 */
public class FourthGradeMathQuestionGenerator implements SubjectQuestionGenerator {
	private static Random RANDOM = new Random();
	private static final String[] ARITHMETIC_OPERATORS = new String[] {"+","-","\u00D7","\u00F7"};
	private static final String[] AREA_UNITS = new String[] {"square feet","square inches","square centimeters","square meters"};
	private static final String[] LENGTH_UNITS = new String[] {"feet","inches","centimeters","meters"};
	private static final String[] LENGTH_UNITS_ABBR = new String[] {"ft","in","cm","m"};
	private static final String[] PLACE_VALUE_VOCABULARY = new String[] {"hundreds","tens","ones","tenths","hundredths"};
	private static final String[] PLACE_VALUE_VOCABULARY_ABBR = new String[] {"100s","10s","1s","10ths","100ths"};
	private static final int[] PRIME_NUMBERS = new int[] {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199};
	private static final Subject subject = new Subject("com.shtick.math.4th");
	private static final HashMap<String,String> dimensionDescriptions = new HashMap<>();
	static {
		// TODO Set descriptions.
	}

	/* (non-Javadoc)
	 * @see com.shtick.apps.sh.core.SubjectQuestionGenerator#getSubject()
	 */
	@Override
	public Subject getSubject() {
		return subject;
	}

	/* (non-Javadoc)
	 * @see com.shtick.apps.sh.core.SubjectQuestionGenerator#generateQuestions(com.shtick.apps.sh.core.Driver, com.shtick.apps.sh.core.UserID, int)
	 */
	@Override
	public Collection<Question> generateQuestions(Driver driver, UserID userID, int count) {
		if(count<=0)
			throw new IllegalArgumentException("One ore more questions must be requested.");
		ArrayList<Question> retval = new ArrayList<>();
		for(int i=0;i<count;i++)
			retval.add(generateQuestion());
		return retval;
	}

	private Question generateQuestion(){
		int type = RANDOM.nextInt(9);
		int a,b,c;
		boolean isStory = RANDOM.nextBoolean();
		boolean translateQuestion = RANDOM.nextBoolean();
		int unknown = RANDOM.nextInt(3);
		switch(type){
		case 0:// Addition
			a = RANDOM.nextInt(100000);
			b = RANDOM.nextInt(100000-a);
			c = a+b;
			return generateArithmeticQuestion(a,b,c,type,unknown,isStory, translateQuestion);
		case 1:// Subtraction
			a = RANDOM.nextInt(100000);
			b = RANDOM.nextInt(a+1);
			c = a-b;
			return generateArithmeticQuestion(a,b,c,type,unknown,isStory, translateQuestion);
		case 2:// Multiplication
			a = RANDOM.nextInt(1000);
			b = RANDOM.nextInt(9)+1;
			c = a*b;
			return generateArithmeticQuestion(a,b,c,type,unknown,isStory, translateQuestion);
		case 3:// Division
			b = RANDOM.nextInt(9)+1;
			c = RANDOM.nextInt(1000);
			a = b*c;
			return generateArithmeticQuestion(a,b,c,type,unknown,isStory, translateQuestion);
		case 4:// Remainders
			a = RANDOM.nextInt(1000);
			b = RANDOM.nextInt(9)+1;
			c = a%b;
			return generateRemainderQuestion(a,b,c,isStory);
		case 5:
			return generatePrimeNumberQuestion();
		case 6:
			return generatePlaceValueQuestion();
		case 7:
			return generateRectangleAreaQuestion();
		default:
			return generateRectanglePerimeterQuestion();
		}
	}
	
	private Question generateRectangleAreaQuestion(){
		HashMap<String,Float> dimensions = new HashMap<>();
		int w = RANDOM.nextInt(20);
		int h = RANDOM.nextInt(20);
		int max = Math.max(w, h);
		int area = w*h;
		int unit = RANDOM.nextInt(LENGTH_UNITS.length);
		int constrainingSquareLength = 100;

		return new Question("<svg width=\""+(constrainingSquareLength+75)+"\" height=\""+(constrainingSquareLength+75)+"\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.1\">\n" + 
				"<rect x=\"25\" y=\"25\" width=\""+(constrainingSquareLength*w/max)+"\" height=\""+(constrainingSquareLength*h/max)+"\" style=\"stroke-width:3;stroke:rgb(0,0,0)\" />\n" + 
				"<text x=\""+((constrainingSquareLength/2)*w/max+15)+"\" y=\""+(constrainingSquareLength*h/max+40)+"\">"+w+" "+LENGTH_UNITS_ABBR[unit]+"</text>\n" +
				"<text x=\""+(constrainingSquareLength*w/max+40)+"\" y=\""+((constrainingSquareLength/2)*h/max+25)+"\">"+h+" "+LENGTH_UNITS_ABBR[unit]+"</text>\n" +
				"</svg>","image/svg+xml","What is the area of this rectangle in "+AREA_UNITS[unit]+".","text/plain",""+area,dimensions,4);
	}
	
	private Question generateRectanglePerimeterQuestion(){
		HashMap<String,Float> dimensions = new HashMap<>();
		int w = RANDOM.nextInt(20);
		int h = RANDOM.nextInt(20);
		int max = Math.max(w, h);
		int perimeter = w*2+h*2;
		int unit = RANDOM.nextInt(LENGTH_UNITS.length);
		int constrainingSquareLength = 100;

		return new Question("<svg width=\""+(constrainingSquareLength+75)+"\" height=\""+(constrainingSquareLength+75)+"\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.1\">\n" + 
				"<rect x=\"25\" y=\"25\" width=\""+(constrainingSquareLength*w/max)+"\" height=\""+(constrainingSquareLength*h/max)+"\" style=\"stroke-width:3;stroke:rgb(0,0,0)\" />\n" + 
				"<text x=\""+((constrainingSquareLength/2)*w/max+15)+"\" y=\""+(constrainingSquareLength*h/max+40)+"\">"+w+" "+LENGTH_UNITS_ABBR[unit]+"</text>\n" +
				"<text x=\""+(constrainingSquareLength*w/max+40)+"\" y=\""+((constrainingSquareLength/2)*h/max+25)+"\">"+h+" "+LENGTH_UNITS_ABBR[unit]+"</text>\n" +
				"</svg>","image/svg+xml","What is the perimeter of this rectangle in "+LENGTH_UNITS[unit]+".","text/plain",""+perimeter,dimensions,4);
	}
	
	private Question generatePlaceValueQuestion(){
		HashMap<String,Float> dimensions = new HashMap<>();
		int place = RANDOM.nextInt(5);
		int startValue = RANDOM.nextInt(5)+1;
		int[] placeValues = new int[] {startValue,startValue+1,startValue+2,startValue+3,startValue+4};
		String number = "";
		for(int i=0;i<5;i++) {
			number+=placeValues[i];
			if(i==2)
				number+=".";
		}
		String[] placeArray = RANDOM.nextBoolean()?PLACE_VALUE_VOCABULARY:PLACE_VALUE_VOCABULARY_ABBR;
		
		if(RANDOM.nextBoolean()) // Get the digit for the place value
			return new Question("You are given this number: "+number,"text/plain","What digit is in the "+placeArray[place]+" place?","text/plain",""+placeValues[place],dimensions,4);

		// Get the place value for the digit.
		ArrayList<Choice> choices = new ArrayList<>(4);
		for(int i=0;i<5;i++)
			choices.add(new Choice("text/plain", ""+placeArray[i], ""+i));
		MultipleChoice multipleChoice = new MultipleChoice("text/plain", "Which of the following words describes the place value of the digit "+placeValues[place]+"?", choices);
		String answerPrompt = JSONEncoder.encode(Marshal.marshal(multipleChoice));
		
		return new Question("You are given this number: "+number,"text/plain",answerPrompt,"choice/radio",""+place,dimensions,4);
	}
	
	private Question generatePrimeNumberQuestion(){
		HashMap<String,Float> dimensions = new HashMap<>();
		int primePosition = RANDOM.nextInt(4);
		
		ArrayList<Choice> choices = new ArrayList<>(4);
		for(int i=0;i<4;i++) {
			if(i==primePosition) {
				choices.add(new Choice("text/plain", ""+PRIME_NUMBERS[RANDOM.nextInt(20)], ""+i));
				continue;
			}
			if(RANDOM.nextBoolean()) {
				choices.add(new Choice("text/plain", ""+(PRIME_NUMBERS[RANDOM.nextInt(4)]*PRIME_NUMBERS[RANDOM.nextInt(4)]*PRIME_NUMBERS[RANDOM.nextInt(4)]), ""+i));
				continue;
			}
			choices.add(new Choice("text/plain", ""+(PRIME_NUMBERS[RANDOM.nextInt(6)]*PRIME_NUMBERS[RANDOM.nextInt(6)]), ""+i));
		}
		MultipleChoice multipleChoice = new MultipleChoice("text/plain", "Which of these numbers is prime.", choices);
		String answerPrompt = JSONEncoder.encode(Marshal.marshal(multipleChoice));
		
		return new Question("Here is a list of numbers.","text/plain",answerPrompt,"choice/radio",""+primePosition,dimensions,4);
	}
	
	private Question generateRemainderQuestion(int a, int b, int c, boolean isStory){
		String prompt;
		String promptType = "text/plain";
		String answerPrompt = "What is the remainder?";
		String answerPromptType = "text/plain";
		String answerValue = ""+c;
		HashMap<String,Float> dimensions = new HashMap<>();
		if(!isStory) {
			prompt = "Find the remainder of "+a+" \u00F7 "+b+".";
		}
		else {
			String name = getRandomName();
			prompt = name+" is camping in the mountains and has "+a+" salted fish. The plan is to stay in the mountains eating "+b+" fish each day until fewer than "+b+" fish are left.";
			answerPrompt = "How many fish will "+name+" have left to eat when leaving camp?";
		}
		return new Question(prompt,promptType,answerPrompt,answerPromptType,answerValue,dimensions,4);
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param type 0=>+, 1=>-, 2=>*, 3=>/
	 * @param unknown If 0, then a should be the unknown, 1=>b, 2=>c
	 * @param isStoryQuestion
	 * @param translateQuestion If true then the answer will be either the number sentence that matches the story, or the story that matches the number sentence.
	 * @return
	 */
	private Question generateArithmeticQuestion(int a, int b, int c, int type, int unknown, boolean isStoryQuestion, boolean translateQuestion){
		String prompt;
		String promptType = "text/plain";
		String answerPrompt;
		String answerPromptType;
		String answerValue;
		
		String unknownValue = ""+((unknown==0)?a:(unknown==1)?b:c);
		
		QuestionWithPrompt numberSentenceQuestion = getArithmeticNumberSentence(a, b, c, type, unknown);
		QuestionWithPrompt wordProblemQuestion = getArithmeticWordProblem(a, b, c, type, unknown);
		if(isStoryQuestion)
			prompt = wordProblemQuestion.getProblemSetup();
		else
			prompt = numberSentenceQuestion.getProblemSetup();
		if(translateQuestion) {
			answerPromptType = "choice/radio";
			ArrayList<Choice> choices = new ArrayList<>(4);
			ArrayList<QuestionWithPrompt> badChoices = new ArrayList<>(3);
			int correctAnswerPosition = RANDOM.nextInt(4);
			answerValue = ""+correctAnswerPosition;
			String correctAnswerContent;
			QuestionWithPrompt badChoice;
			if(isStoryQuestion) {
				answerPrompt = "Which of the following number sentences can be used to answer this question: "+wordProblemQuestion.getPrompt();
				correctAnswerContent = numberSentenceQuestion.getProblemSetup();
				if(a!=b) {
					if((type&1)==1)
						badChoice = getArithmeticNumberSentence(b, a, c, type, (unknown==2)?unknown:unknown^1);
					else
						badChoice = getArithmeticNumberSentence(b+1, a, c, type^1, (unknown==2)?unknown:unknown^1);
				}
				else {
					badChoice = getArithmeticNumberSentence(a+1, b, c, type, unknown);
				}
				badChoices.add(badChoice);
				
				badChoice = getArithmeticNumberSentence(a, b, c, type^1, unknown);
				badChoices.add(badChoice);

				badChoice = getArithmeticNumberSentence(b, a, c, type^1, (unknown==2)?unknown:unknown^1);
				badChoices.add(badChoice);
			}
			else {
				answerPrompt = "Which of the following story problems can be solved using this number sentence?";
				correctAnswerContent = wordProblemQuestion.getProblemSetup()+" "+wordProblemQuestion.getPrompt();
				if(a!=b) {
					if((type&1)==1)
						badChoice = getArithmeticWordProblem(b, a, c, type, (unknown==2)?unknown:unknown^1);
					else
						badChoice = getArithmeticWordProblem(b+1, a, c, type^1, (unknown==2)?unknown:unknown^1);
				}
				else {
					badChoice = getArithmeticWordProblem(a+1, b, c, type, unknown);
				}
				badChoices.add(badChoice);
				
				badChoice = getArithmeticWordProblem(a, b, c, type^1, unknown);
				badChoices.add(badChoice);

				badChoice = getArithmeticWordProblem(b, a, c, type^1, (unknown==2)?unknown:unknown^1);
				badChoices.add(badChoice);
			}
			
			for(int i=0;i<4;i++) {
				if(i==correctAnswerPosition) {
					choices.add(new Choice("text/plain", correctAnswerContent, answerValue));
					continue;
				}
				badChoice = badChoices.remove(RANDOM.nextInt(badChoices.size()));
				if(isStoryQuestion)
					choices.add(new Choice("text/plain", badChoice.getProblemSetup(), ""+i));
				else
					choices.add(new Choice("text/plain", badChoice.getProblemSetup()+" "+badChoice.getPrompt(), ""+i));
			}
			MultipleChoice multipleChoice = new MultipleChoice("text/plain", answerPrompt, choices);
			answerPrompt = JSONEncoder.encode(Marshal.marshal(multipleChoice));
		}
		else {
			answerPromptType = "text/plain";
			if(isStoryQuestion)
				answerPrompt = wordProblemQuestion.getPrompt();
			else
				answerPrompt = numberSentenceQuestion.getPrompt();
			answerValue = unknownValue;
		}
		return new Question(prompt,promptType,answerPrompt,answerPromptType,answerValue,new HashMap<String, Float>(0),4);
	}
	
	private QuestionWithPrompt getArithmeticNumberSentence(int a, int b, int c, int type, int unknown) {
		String[] parts = new String[]{""+a,ARITHMETIC_OPERATORS[type],""+b,"=",""+c};
		parts[unknown*2] = "?";
		String numberSentence = "";
		for(String part:parts) {
			if(numberSentence.length()>0)
				numberSentence+=" ";
			numberSentence+=part;
		}
		return new QuestionWithPrompt(numberSentence, (unknown==2)?"What is the answer?":"What is the missing value?");
	}
	
	private QuestionWithPrompt getArithmeticWordProblem(int a, int b, int c, int type, int unknown) {
		String wordProblem;
		String answerPrompt;
		String name = getRandomName();
		StandardNoun object = getRandomCollectible();
		switch(type){
		case 0:// Addition
			switch(unknown){
			case 0:
				wordProblem = name+" came from a family that was obsessed with "+object.get(Case.ACCUSATIVE, false)+" and had a personal collection of "+object.get(Case.ACCUSATIVE, false)+". "+name+" inherited "+b+" "+object.get(Case.ACCUSATIVE, b==1)+" and now has "+c+" "+object.get(Case.ACCUSATIVE, c==1)+".";
				answerPrompt = "How many "+object.get(Case.ACCUSATIVE, false)+" did "+name+" have to begin with?";
				break;
			case 1:
				wordProblem = name+" came from a family that was obsessed with "+object.get(Case.ACCUSATIVE, false)+" and had "+((a>10)?"amassed":"")+" a personal collection of "+a+" "+object.get(Case.ACCUSATIVE, a==1)+". "+name+" inherited the "+object.get(Case.ACCUSATIVE, false)+" of a relative and now has "+c+" "+object.get(Case.ACCUSATIVE, c==1)+".";
				answerPrompt = "How many "+object.get(Case.ACCUSATIVE, false)+" did "+name+" inherit?";
				break;
			default:
				wordProblem = name+" came from a family that was obsessed with "+object.get(Case.ACCUSATIVE, false)+" and had "+((a>10)?"amassed":"")+" a personal collection of "+a+" "+object.get(Case.ACCUSATIVE, a==1)+". "+name+" inherited "+b+" "+object.get(Case.ACCUSATIVE, b==1)+".";
				answerPrompt = "How many "+object.get(Case.ACCUSATIVE, false)+" does "+name+" have now?";
				break;
			}
			break;
		case 1:// Subtraction
			switch(unknown){
			case 0:
				wordProblem = name+" had a "+object.get(Case.ACCUSATIVE, true)+" collection, but the obsession with collecting "+object.get(Case.ACCUSATIVE, false)+" led to the ruin of all that "+name+" possessed. House, wealth, wife, and children were all lost. This of course left "+name+" with the problem of not having a place to store the collection, so "+name+" decided to sell some of the collection to rent a storage facility to live in. "+name+" sold "+b+" "+object.get(Case.ACCUSATIVE, b==1)+" and was left with "+c+" "+object.get(Case.ACCUSATIVE, c==1)+".";
				answerPrompt = "How many "+object.get(Case.ACCUSATIVE, false)+" did "+name+" have before selling some?";
				break;
			case 1:
				wordProblem = name+" had a "+object.get(Case.ACCUSATIVE, true)+" collection with "+a+" "+object.get(Case.ACCUSATIVE, a==1)+", but the obsession with collecting "+object.get(Case.ACCUSATIVE, false)+" led to the ruin of all that "+name+" possessed. House, wealth, wife, and children were all lost. This of course left "+name+" with the problem of not having a place to store the collection, so "+name+" decided to sell some of the collection to rent a storage facility to live in. After selling some of the "+object.get(Case.ACCUSATIVE, false)+" "+name+" was left with "+c+" "+object.get(Case.ACCUSATIVE, c==1)+".";
				answerPrompt = "How many "+object.get(Case.ACCUSATIVE, false)+" did "+name+" have to sell?";
				break;
			default:
				wordProblem = name+" had a "+object.get(Case.ACCUSATIVE, true)+" collection with "+a+" "+object.get(Case.ACCUSATIVE, a==1)+", but the obsession with collecting "+object.get(Case.ACCUSATIVE, false)+" led to the ruin of all that "+name+" possessed. House, wealth, wife, and children were all lost. This of course left "+name+" with the problem of not having a place to store the collection, so "+name+" decided to sell some of the collection to rent a storage facility to live in. "+name+" sold "+b+" "+object.get(Case.ACCUSATIVE, b==1)+".";
				answerPrompt = "How many "+object.get(Case.ACCUSATIVE, false)+" does "+name+" have now?";
				break;
			}
			break;
		case 2:// Multiplication
			StandardNoun object2 = getRandomCollectible();
			while(object.get(Case.NOMINATIVE, true).equals(object2.get(Case.NOMINATIVE, true)))
				object2 = getRandomCollectible();
			switch(unknown){
			case 0:
				wordProblem = name+" owned some "+object.get(Case.ACCUSATIVE, false)+" and found a deal whereby each "+object.get(Case.ACCUSATIVE, true)+" could be exchanged for "+b+" "+object2.get(Case.ACCUSATIVE, b==1)+". "+name+" traded in some "+object.get(Case.ACCUSATIVE, false)+", netting "+name+" a stock of "+c+" "+object2.get(Case.ACCUSATIVE, c==1)+".";
				answerPrompt = "How many "+object.get(Case.ACCUSATIVE, false)+" did "+name+" trade?";
				break;
			case 1:
				wordProblem = name+" owned "+a+" "+object.get(Case.ACCUSATIVE, a==1)+" and found a deal whereby each "+object.get(Case.ACCUSATIVE, true)+" could be exchanged for some "+object2.get(Case.ACCUSATIVE, false)+". "+name+" traded in some "+object.get(Case.ACCUSATIVE, false)+", netting "+name+" a stock of "+c+" "+object2.get(Case.ACCUSATIVE, c==1)+".";
				answerPrompt = "How many "+object2.get(Case.ACCUSATIVE, false)+" did "+name+" receive for each "+object.get(Case.ACCUSATIVE, true)+" traded?";
				break;
			default:
				wordProblem = name+" owned "+a+" "+object.get(Case.ACCUSATIVE, a==1)+" and found a deal whereby each "+object.get(Case.ACCUSATIVE, true)+" could be exchanged for "+b+" "+object2.get(Case.ACCUSATIVE, b==1)+". "+name+" found the deal irresistable, so the entire stock of "+object.get(Case.ACCUSATIVE, false)+" was traded in.";
				answerPrompt = "How many "+object2.get(Case.ACCUSATIVE, false)+" did "+name+" have after trading in all the "+object.get(Case.ACCUSATIVE, false)+"?";
				break;
			}
			break;
		default:// Division
			switch(unknown){
			case 0:
				wordProblem = name+" had "+b+" "+((b==1)?"child":"children")+" and a very valuable collection consisting of "+object.get(Case.ACCUSATIVE, false)+". "+name+" was also dead. The executor of the estate found that the will, written very carefully, many year ago stipulated that the "+object.get(Case.ACCUSATIVE, true)+" collection was to be divided up evenly between all of the children. Each child will receive "+c+" "+object.get(Case.ACCUSATIVE, c==1)+".";
				answerPrompt = "How many "+object.get(Case.ACCUSATIVE, false)+" did "+name+" have?";
				break;
			case 1:
				wordProblem = name+" had children and a very valuable collection consisting of "+a+" "+object.get(Case.ACCUSATIVE, a==1)+". "+name+" was also dead. The executor of the estate found that the will, written very carefully, many year ago stipulated that the "+object.get(Case.ACCUSATIVE, true)+" collection was to be divided up evenly between all of the children. Each child will receive "+c+" "+object.get(Case.ACCUSATIVE, c==1)+".";
				answerPrompt = "How many children did "+name+" have?";
				break;
			default:
				wordProblem = name+" had "+b+" "+((b==1)?"child":"children")+" and a very valuable collection consisting of "+a+" "+object.get(Case.ACCUSATIVE, a==1)+". "+name+" was also dead. The executor of the estate found that the will, written very carefully, many year ago stipulated that the "+object.get(Case.ACCUSATIVE, true)+" collection was to be divided up evenly between all of the children, with any remaining items to be used as a prize for a boxing tournament that the children were requested to compete in as the main event for the funeral.";
				answerPrompt = "How many "+object.get(Case.ACCUSATIVE, false)+" does each child get before the funeral?";
				break;
			}
			break;
		}
		return new QuestionWithPrompt(wordProblem, answerPrompt);
	}
	
	private static final String[] STANDARD_NOUN_COLLECTIBLES = new String[] {
		"baseball card",
		"pokemon card",
		"postcard",
		"stamp",
		"plate",
		"knife",
		"thimble",
		"spoon",
		"Beanie Baby",
		"rubber band",
		"shoe",
		"hat",
		"bug",
		"butterfly",
		"rock",
	};
	
	private static final String[] NAMES = new String[]{
			"Adison",
			"Al",
			"Albert",
			"Alice",
			"Alison",
			"Amber",
			"Andrew",
			"Andy",
			"Art",
			"Arthur",
			"Bartolo",
			"Bob",
			"Bulleta",
			"Carlos",
			"Casey",
			"Cassey",
			"Chelsea",
			"Claire",
			"Clairance",
			"Cora",
			"Crystal",
			"Dallin",
			"David",
			"Ed",
			"Edison",
			"Elthon",
			"Enoch",
			"Ephraim",
			"Fred",
			"Freddy",
			"Fredrick",
			"George",
			"Gladys",
			"Jared",
			"Jane",
			"Jasher",
			"Jill",
			"John",
			"Justin",
			"Kelly",
			"Kim",
			"Lillian",
			"Math",
			"Matt",
			"Matthew",
			"Nishelle",
			"Michael",
			"Nathan",
			"Nicholas",
			"Oscar",
			"Pam",
			"Peter",
			"Richard",
			"Robert",
			"Ronald",
			"Ronni",
			"Scott",
			"Sean",
			"Shia",
			"Solace",
			"Steve",
			"Steven",
			"Susan",
			"Thomas",
			"Tom",
	};
	
	private String getRandomName(){
		return NAMES[RANDOM.nextInt(NAMES.length)];
	}
	
	private StandardNoun getRandomCollectible(){
		return new StandardNoun(STANDARD_NOUN_COLLECTIBLES[RANDOM.nextInt(STANDARD_NOUN_COLLECTIBLES.length)]);
	}
	
	private class QuestionWithPrompt{
		private String problemSetup;
		private String prompt;
		
		/**
		 * 
		 * @param problemSetup
		 * @param prompt
		 */
		public QuestionWithPrompt(String problemSetup, String prompt) {
			super();
			this.problemSetup = problemSetup;
			this.prompt = prompt;
		}
		
		/**
		 * @return the question
		 */
		public String getProblemSetup() {
			return problemSetup;
		}
		/**
		 * @return the prompt
		 */
		public String getPrompt() {
			return prompt;
		}
	}
	
	/**
	 * Addition (sums to under 100,000)
	 * Subtraction (to under 100,000)
	 * Story Problems (Dimensions below)
	 * Multiplication (to ~1000)
	 * Division (1000's divided by 1's)
	 * Translate Words to # Sentences
	 * Remainders
	 * Prime/Composite
	 * Vocabulary:
	 *  - 10ths
	 *  - 100ths
	 * 
	 * Factor Pairs
	 * Factors
	 * Mixed Operations
	 * Simplifying Fractions
	 * Adding/Subtracting like fractions
	 * a b/c = d/c
	 * Decimals down to 100ths
	 * Vocabulary:
	 *  - Numerator
	 *  - Denominator
	 */
}
