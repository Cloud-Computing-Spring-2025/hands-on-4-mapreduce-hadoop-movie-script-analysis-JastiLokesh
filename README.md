

# Movie Script Analysis using Hadoop MapReduce

This repository is designed to test MapReduce jobs using a simple word count dataset.

## Objectives


1. **Understand Hadoop's Architecture:** Learn how Hadoop's distributed file system (HDFS) and MapReduce framework work together to process large datasets.
2. **Build and Deploy a MapReduce Job:** Gain experience in compiling a Java MapReduce program, deploying it to a Hadoop cluster, and running it using Docker.
3. **Interact with Hadoop Ecosystem:** Practice using Hadoop commands to manage HDFS and execute MapReduce jobs.
4. **Work with Docker Containers:** Understand how to use Docker to run and manage Hadoop components and transfer files between the host and container environments.
5. **Analyze MapReduce Job Outputs:** Learn how to retrieve and interpret the results of a MapReduce job.

## Setup and Execution

### 1. **Start the Hadoop Cluster**

Run the following command to start the Hadoop cluster:

```bash
docker compose up -d
```

### 2. **Build the Code**

Build the code using Maven:

```bash
mvn install
```

### 3. **Move JAR File to Shared Folder**

Move the generated JAR file to a shared folder for easy access:

```bash
mv target/*.jar input/code/
```

### 4. **Copy JAR to Docker Container**

Copy the JAR file to the Hadoop ResourceManager container:

```bash
docker cp input/code/hands-on2-movie-script-analysis-1.0-SNAPSHOT.jar resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 5. **Move Dataset to Docker Container**

Copy the dataset to the Hadoop ResourceManager container:

```bash
docker cp input/data/movie_dialogues.txt resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 6. **Connect to Docker Container**

Access the Hadoop ResourceManager container:

```bash
docker exec -it resourcemanager /bin/bash
```

Navigate to the Hadoop directory:

```bash
cd /opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 7. **Set Up HDFS**

Create a folder in HDFS for the input dataset:

```bash
hadoop fs -mkdir -p /input/dataset
```

Copy the input dataset to the HDFS folder:

```bash
hadoop fs -put ./movie_dialogues.txt /input/dataset
```

### 8. **Execute the MapReduce Job**

Run your MapReduce job using the following command:

```bash
hadoop jar /opt/hadoop-3.2.1/share/hadoop/mapreduce/hands-on2-movie-script-analysis-1.0-SNAPSHOT.jar com.movie.script.analysis.MovieScriptAnalysis /input/dataset/input.txt /output
```

### 9. **View the Output**

To view the output of your MapReduce job, use:

```bash
hadoop fs -cat /output/*
```

### 10. **Copy Output from HDFS to Local OS**

To copy the output from HDFS to your local machine:

1. Use the following command to copy from HDFS:
    ```bash
    hdfs dfs -get /output /opt/hadoop-3.2.1/share/hadoop/mapreduce/
    ```

2. use Docker to copy from the container to your local machine:
   ```bash
   exit 
   ```
    ```bash
    docker cp resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/output/ input/output/
    ```
3. Commit and push to your repo so that we can able to see your output


### REPORT

## Overview
This project performs **Movie Script Analysis** using **Hadoop MapReduce**. The analysis involves processing movie dialogues to extract insights such as:
- **Most frequent words by character**
- **Dialogue length analysis**
- **Unique words spoken by each character**


## Approach and Implementation

### **Mapper and Reducer Logic**

#### **1. CharacterWordMapper & CharacterWordReducer**
- **Mapper:** Extracts words from each character’s dialogue and emits (`word, 1`).
- **Reducer:** Aggregates the counts of each word spoken by characters.

#### **2. DialogueLengthMapper & DialogueLengthReducer**
- **Mapper:** Extracts the character name and calculates the length of their dialogue.
- **Reducer:** Sums up the total dialogue length for each character.

#### **3. UniqueWordsMapper & UniqueWordsReducer**
- **Mapper:** Emits each unique word spoken by a character.
- **Reducer:** Aggregates unique words spoken by each character.

**Challenges Faced & Solutions**

While executing the MapReduce jobs, I have encountered error in recognizing of aruguments for this 
```
hadoop jar /opt/hadoop-3.2.1/share/hadoop/mapreduce/hands-on2-movie-script-analysis-1.0-SNAPSHOT.jar com.movie.script.analysis.MovieScriptAnalysis /input/dataset/input.txt /output
```

com.movie.script.analysis.MovieScriptAnalysis as one

/input/dataset/input.txt as two

/output as three

Then I put an extra stringarray to change the arguments order, so that only two and three will go with the command



### Input & Output
### **Input Format**
A movie script dialogue dataset where each line follows the format:
```
JACK: The ship is sinking! We have to go now.
ROSE: I won’t leave without you.
JACK: We don’t have time, Rose!
```

### **Expected Output**
#### **1. Most Frequently Spoken Words by Characters**
```
the 3
we 3
have 2
to 2
now 1
without 1
```

#### **2. Total Dialogue Length per Character**
```
JACK 54
ROSE 25
```

#### **3. Unique Words Used by Each Character**
```
JACK [the, ship, is, sinking, we, have, to, go, now, don’t, time, rose]
ROSE [i, won’t, leave, without, you]
```

#### **4. Hadoop Counter Output**
```
Total Lines Processed: 3
Total Words Processed: 18
Total Characters Processed: 79
Total Unique Words Identified: 13
Number of Characters Speaking: 2
```
