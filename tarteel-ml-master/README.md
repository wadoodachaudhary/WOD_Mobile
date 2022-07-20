# Recitation

This repo is designed to house code related to recitation matching related tasks. 


Specifically, things like:

* Model selection :white_check_mark:
* Preprocessing of data :sound:
* Model training, validation, and and iteration :repeat:
* Demos :rocket:


### Prerequisites 

We use Python 3.7 for our development.
However, any Python above 3.6 should work.
For audio pre-processing, we use `ffmpeg` and `ffprobe`.
Make sure you install these using your system package manager.

**Mac OS**

```bash
brew install ffmpeg
```

**Linux**

```bash
sudo apt install ffmpeg
```

Then install the Python dependencies from [`requirements.txt`](requirements.txt).

```bash
pip3 install -r requirements.txt
```

### Usage

Use the `-h`/`--help` flag for more info on how to use each script.

This repo is structured as follows:

**Root**

* [`download.py`]: Download the Tarteel dataset
* [`create_train_test_split.py`]: Create train/test/validation split csv files.
* `generate_alphabet|vocabulary.py`: Generate all unique letters/ayahs in the Quran in a text file.
* [`generate_csv_deepspeech.py`]: Create a CSV file for training with DeepSpeech.

