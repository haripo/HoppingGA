# HoppingGA

Learn how to play hopping (= pogo stick) by genetic algorithm.

![Demo](images/generation50.gif)

## Model

![Model](images/model.png)

## Genetic Algorithm

A gene is implemented as an integer array.
Its (2n)-th element represents stretch speed of the model's arm,
and (2n+1)-th element represents stretch speed of him/her leg.
I defined fitness as how long the model stands + how far the model moves.


- gene length = 300
- crossover rate = 0.6
- selection = tournament
- mutation rate = 0.01
- population = 50
- elite = 1

