package spell_checker;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.JaroWinklerDistance;
import org.apache.lucene.search.spell.LevenshteinDistance;
import org.apache.lucene.search.spell.LuceneLevenshteinDistance;
import org.apache.lucene.search.spell.NGramDistance;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

@SuppressWarnings("unused")
public class Spell_Checker
{
    public void check_txt_dictionary(String input_word) throws IOException
    {
        // Δημιουργία του ευρετηρίου.
        Directory directory = FSDirectory.open(Paths.get("Index"));
        //PlainTextDictionary txt_dict = new PlainTextDictionary(Paths.get("eng_dictionary.txt"));
        PlainTextDictionary txt_dict = new PlainTextDictionary(Paths.get("greek_dictionary.txt"));
        @SuppressWarnings("resource")
		SpellChecker checker = new SpellChecker(directory);

        System.out.print("\nΗ δημιουργία του ευρετηρίου από το .txt λεξιλόγιο χρειάστηκε... ");
        long start_time = System.currentTimeMillis();
            checker.indexDictionary(txt_dict, new IndexWriterConfig(new KeywordAnalyzer()), false);
            directory.close();
        long end_time = System.currentTimeMillis();
        System.out.println((end_time - start_time)/1000 + " δευτερόλεπτα.\n");
        
        
        // Αναζήτηση και παρουσίαση προτεινώμενων λέξεων επιλέγοντας ένα string distance.
        //Επειδή οι παρακάτω εντολές είναι αντικρουόμενες τις βάζουμε σε σχόλια και τρέχουμε μια τη φορά.
        //checker.setStringDistance(new JaroWinklerDistance());
        //checker.setStringDistance(new LevenshteinDistance());
        //checker.setStringDistance(new LuceneLevenshteinDistance());
        checker.setStringDistance(new NGramDistance()); 
        
        String[] suggestions = checker.suggestSimilar(input_word, 5);
        
        System.out.println("Με '" + input_word + "' μήπως εννοείται:");
        for(String suggestion : suggestions)
            System.out.println("\t" + suggestion);
    }
    
    //Η συνάρτηση main.
    public static void main(String[] args) throws IOException, Throwable
    {
        @SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
        Spell_Checker spell_checker = new Spell_Checker();
  
        System.out.print("\nΓράψε μια λέξη προς έλεγχο: ");
        String input_word = scan.next();

        spell_checker.check_txt_dictionary(input_word);
    }
}