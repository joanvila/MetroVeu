# MetroVeu
Android application for the FIB PES subject.

Android Studio version: 1.4

##How to run the tests

###Customizating build.gradle

Add the following dependencies:

- androidTestCompile 'com.android.support.test:runner:0.4'

- androidTestCompile 'com.android.support.test:rules:0.4'

- androidTestCompile 'com.android.support.test.espresso:espresso-core:2.2.1'

- Inside the defaultConfig attributes, add this one:
testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"

###Editing test configurations

1. Go to Run > Edit Configurations...
2. Click on the "**+**" to add a new configuration to run the tests and select **Android Tests**
3. Name the test as you want. I've named it MetroVeu Tests
4. Select the **mobile** module
5. Add a Specific instrumentation runner: android.support.test.runner.AndroidJUnitRunner
6. On target device select Show chooser dialog
7. Press OK to accept and close the window

###Running the tests

Right click on a test class and select **Run 'AplicationTest'** or whatever the test is named.

Enjoy TDD. Red green refactor.

[Useful espresso cheat sheet to make tests](https://google.github.io/android-testing-support-library/docs/espresso/cheatsheet/)
