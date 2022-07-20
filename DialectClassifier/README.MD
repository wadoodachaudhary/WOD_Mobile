 Automatic Arabic Dialect Detection Task 


# Requirements
* Python (tested with v.2.7.5)
* Multi-class SVM (http://www.cs.cornell.edu/people/tj/svm_light/svm_multiclass.html)

# Provided data:
* We provide data for five Arabic dialects: Egyptian (EGY), Levantine (LAV), Gulf (GLF), North African (NOR), and Modern Standard Arabic (MSA).


***VarDial'2017 shared task*** shared data, and features.
* The baseline for VarDial'2017 is using data/train.vardial2017/ and data/dev.vardial2017/ for training and development ***default***
* For each dialect, there are two features files:
* $dialect.words -- lexical features generated using LVCSR- generated using QCRI MGB-2 submission.
* $dialect.ivec -- i-vector based on bottleneck features, with a fixed length of 400 per utterance.
* wav.lst -- link to the original audio files; WAVE audio, Microsoft PCM, 16 bit, mono 16000 Hz.
* ***Baseline***-- bottleneck iVectors 57.28% accuracy and lexical features 48.43%. 

*

# Sample code 

Run 'run.sh' for an example of the code and the data
* features=phones -- you can use words, phones or ivectors;
* context=6 -- for some features, less context might be enough;
* NOTE 1: The regularization parameters can be optimized for better performance.
* NOTE 2: System combination can be explored as well.


