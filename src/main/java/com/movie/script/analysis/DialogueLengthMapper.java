package com.movie.script.analysis;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class DialogueLengthMapper extends Mapper<Object, Text, Text, IntWritable> {
    private final static IntWritable wordCount = new IntWritable();
    private Text character = new Text();

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        if (line.contains(":")) {
            String[] parts = line.split(":", 2);
            String characterName = parts[0].trim();
            String dialogue = parts[1].trim();
            
            character.set(characterName);
            wordCount.set(dialogue.length());
            context.write(character, wordCount);
            
            // Increment counter for unique characters
            context.getCounter("Movie Analysis Counters", "Number of Characters Speaking").increment(1);
        }
    }
}
