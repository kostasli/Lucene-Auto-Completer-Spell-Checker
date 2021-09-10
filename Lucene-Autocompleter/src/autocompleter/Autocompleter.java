package autocompleter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.search.suggest.Lookup;
import org.apache.lucene.search.suggest.Lookup.LookupResult;
import org.apache.lucene.search.suggest.analyzing.AnalyzingInfixSuggester;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Autocompleter 
{
    public void get_suggested_words(String input_word) throws IOException
    {
        // Δημιουργία ευρετηρίου.
        Directory directory = FSDirectory.open(Paths.get("Index"));
        //PlainTextDictionary txt_dict = new PlainTextDictionary(Paths.get("eng_dictionary.txt"));
        PlainTextDictionary txt_dict = new PlainTextDictionary(Paths.get("greek_dictionary.txt"));
        @SuppressWarnings("resource")
		SpellChecker checker = new SpellChecker(directory);

        System.out.print("\nΗ κατασκευή του ευρετηρίου από το .txt λεξιλόγιο χρειάστηκε... ");
        long start_time = System.currentTimeMillis();
            checker.indexDictionary(txt_dict, new IndexWriterConfig(new KeywordAnalyzer()), false);
        long end_time = System.currentTimeMillis();
        System.out.println((end_time - start_time)/1000 + " δευτερόλεπτα.");

        
        // Αναζήτηση και παρουσίαση προτεινώμενων λέξεων.
        StandardAnalyzer analyzer = new StandardAnalyzer();
        @SuppressWarnings("resource")
		AnalyzingInfixSuggester suggester = new AnalyzingInfixSuggester(directory, analyzer);
        
        //PlainTextDictionary dictionary = new PlainTextDictionary(Paths.get("eng_dictionary.txt"));
        PlainTextDictionary dictionary = new PlainTextDictionary(Paths.get("greek_dictionary.txt"));
        
        System.out.print("Η αναζήτηση του ευρετηρίου για προτάσεις χρειάστηκε... ");
        start_time = System.currentTimeMillis();
            suggester.build(dictionary);
        end_time = System.currentTimeMillis();
        System.out.println((end_time - start_time)/1000 + " δευτερόλεπτα.\n");
        
        List<Lookup.LookupResult> suggested_words = suggester.lookup(input_word, 5, true, true);
        
        System.out.println("Με '" + input_word + "' μήπως εννοείται:");
        for(LookupResult suggested_word : suggested_words) 
            System.out.println("\t" + suggested_word.key.toString());
        
        directory.close();
    }
    
    
    public static void main(String[] args) throws IOException
    {
        @SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
        Autocompleter autocompleter = new Autocompleter();
        
        
        System.out.print("Ξεκινήστε να γράφετε μια λέξη: ");
        String input_word = scan.next();
        
        autocompleter.get_suggested_words(input_word);
    }
}
