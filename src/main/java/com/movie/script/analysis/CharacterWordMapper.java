package com.movie.script.analysis;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class CharacterWordMapper extends Mapper<Object, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        // Check if line contains dialogue format
        if (line.contains(":")) {
            String[] parts = line.split(":", 2);
            String dialogue = parts[1].trim().toLowerCase();
            
            // Update counters
            context.getCounter("Movie Analysis Counters", "Total Lines Processed").increment(1);
            context.getCounter("Movie Analysis Counters", "Total Characters Processed").increment(line.length());
            
            StringTokenizer tokenizer = new StringTokenizer(dialogue);
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                word.set(token);
                context.write(word, one);
                context.getCounter("Movie Analysis Counters", "Total Words Processed").increment(1);
            }
        }
    }
}