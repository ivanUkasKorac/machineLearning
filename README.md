# Machine Learnig using decision tree (ID3)

The program takes in 3 argument values: 
  1. first the path to the train set file
  2. second the path to the test set file
  (OPTIONAL) 3. Number for limiting the maximum debth of tree branch

For the program to run properly configure the train and test set in the next way:
- this algorithm uses two datasets, one for training (train set) and the other for prediction (test set)
- they are both written in CSV format (where the comma "," won't appear as a character in the features value), and are written the same way:
    - first row is the header which tells you the feature name, again every feature seperated by comma ","
    - all the other rows are values of the corresponding features, where each row is a combination of features that results in a solution
    - the solution or the class label is the last feature name and the last value in every row
    - THERE SHOULD ALLWAYS BE AT LEAST ONE FEATURE (it being the class label) FOR THE PROGRAM TO WORK 
    - EXAMPLE:
    weather,temperature,humidity,wind,play
    sunny,hot,high,weak,no
    rainy,cold,low,strong,no
    ...
    - TEST SET SHOULD ALSO HAVE A CLASS LABEL AND SOLUTION FOR CHECKING ACCURACY LATER
    
What the program prints:

- the program also calculates and writes down information gain (IG) for each feature when it calculates the decision tree
- then it writes down the decision tree in the next format : level:feature_name=feature_value
  - level is the debth of the node in the tree
  - feature name is the name of the feature in that node
  - feature value is the value of the branch of the tree that we are currently on
  -EXAMPLE:
  1:weather=cloudy yes
  1:weather=rainy 2:wind=strong no
  1:weather=rainy 2:wind=weak yes
  1:weather=sunny 2:humidity=normal yes
  1:weather=sunny 2:humidity=high no
- furthermore the program prints the prediction for the test set, each prediction seperated by space in a row:
  -EXAMPLE:
  yes yes yes yes no yes yes yes no yes yes no yes no no yes yes yes yes
- then it checks and prints the accuracy by dividing correct solutions to them all
- it also prints out the confusion matrix which tells us, for each class label value (values of all the posible solutions), how much it missed
  - it has dimensions of Y x Y (Y is the number of values of the class label - solution)
  - it is a grid of predicted values and true values and each cell shows the number of examples for the combination of possible values
  - EXAMPLE:
    -if the class label was play and the possible values were yes and no, it would be a 2 x 2 matrix:
    (yes yes) (no yes)
    (yes no)  (no no)
    - for the volleyball problem:
    4 7
    1 7
- its also possible to give the program a number for limiting the debth of the tree, the number is the biggest debth of of each branch in the tree:
-EXAMPLE for limit of 1:
  1:weather=cloudy yes
  1:weather=rainy yes
  1:weather=sunny no
  
    
