SimpleNeuralNetwork
===================
Simple example of an artificial neural network trained with backpropagation.
It is not optimised and learns to recognize a horizontal bar in a 3x3 square:

XXX

OOO

OOO

or

OOO

XXX

OOO

or

OOO

OOO

XXX
###Compile###
`ant -f . -Dnb.internal.action.name=rebuild clean jar`

###Run###
`java -classpath dist/SimpleNeuralNetwork.jar simpleneuralnetwork.SimpleNeuralNetwork`

###Comments###
You will find some comments in the `trialsAndResults.txt` file. This networks performs suprisingly badly on this example, as it needs a 5000 examples of training data in order to reach a 90% accuracy. The problem being really simple, there are only `2^9` input combinations.
A few ideas of where the problem comes from are:
- Error in backpropagation implementation?
- Bad network structure for this problem. The model complexity is much higher than the problem complexity.
- Backpropagation is not that efficient.
- Any idea?
