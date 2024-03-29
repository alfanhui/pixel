<p align="center"><img src="https://github.com/alfanhui/pixel/raw/master/logo.png?raw=true"/></p>

# pixel

<p align="center"><img src="./Screenshot-2023-1.png"/></p>

A digital camera takes a picture, whether it is of your face or a place you love, we can take that picture and say 'This is me' or 'This is my home' and we can understand this connection. But, what if I reminded you that a camera is limited, by its image sensor, its color depth, by its field of view: would that mean that your face was pre-determined in the camera? That by setting the right values, we can produce images that you recognize as yourself or your house? Therefore, if we ran through every possible combination picture a camera could possibly take, would we not see your face or my face or your house, now, from 20 years ago, or even see into the future?<br>

This is my philosophical program, made in 3 stages:

## First stage<p style='color:green'>(COMPLETE)</p>

Program generates sequences or random pixelated images. Of course the chances of a recognisable picture are so rare it might as well not happen at all. (but you're free to try!)

[Video](https://youtu.be/fpI5EB1cROw)

## Second stage<p style='color:green'>(COMPLETE)

Implement a camera feed or jpg importer to the program and converting the image to the dimensions and scale of the first stage. With any image and the understanding of how my sequencing algorithm works, I can determine whereabouts such an image is in its completion. (if 0% is full white image and 100% is a full black image)

## Third stage<p style='color:blue'>(PARTIAL)</p>

Make the program interactive. An idea would be to take a still camera image, then in the sequence, rewind the algorithm by a few 10,000 iterations (or how ever many iterations can happen in 10 seconds), then to play the sequence - as if we were running the program through sequence, with a pause of a few seconds to show you that your camera feed is represented in the image, ending with some iterations back into oblivion.

## How to Run

You will need JDK, such as opendjk installed, with JAVA_HOME set to the correct path.

Open Terminal:

```bash
./mvnw compile package exec:java
```
