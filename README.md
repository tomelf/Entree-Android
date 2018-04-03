# Entree-Android
Entrée: a web service for users to share review per dish instead of review per restaurant

<a href="http://www.youtube.com/watch?feature=player_embedded&v=xrwoEwb6RDg" target="_blank"><img src="http://img.youtube.com/vi/xrwoEwb6RDg/0.jpg" alt="IMAGE ALT TEXT HERE" width="400" height="300" border="10" /></a>

## Team Members

- [Sam Ling](https://github.com/thling)
- [Ellen Lai](https://github.com/yiyulai)
- [Bruce Weng](https://github.com/BruceWeng)

We proposed Entrée in the Hack the Anvil (Hackathon) 2015 and finished in the top 14 among 100+ teams. The prototype was presented to the hackathon sponsors, such as Apple, Cisco and Microsoft.


## Inspiration

We stumbled upon a problem where we did not have easy access to the best menus in the town we live in. Surely there is Yelp, Google+ Locals, and many other great tools that review restaurants, but none of them presents review for individual dishes. Some of the listings don't even have the proper menus!

We decided to hack together a service that allows people to contribute towards a better way to review individual menu, giving the users better understanding of the dishes in the restaurant.

## How it works

We have made an Android app that serves as a front-end for users to contribute to the details of the menus. The service works by crowd sourcing information from our users in the same way as Google Maps, where each of them can then submit new menus they discovered. A few things the users can do to help others understand the restaurants better:

- Submit new menu that are not already on Entrée, provide pricing and descriptions on the dishes
- Propose changes to existing menus on Entrée
- Follow people for their distinct (and similar to the users') taste
- Review each menu, not just the restaurant

## Challenges we ran into

We ran into a few problems both in the front-end and back-end. For the front-end, we had a few performance issues, which some of them are due to our limited access to resources on our hosting platform. In the back-end, since we were developing from scratch using express.js and MongoDB and this is our first time using these tools, we had many problems structuring the code and exploring the routing for the application. We also did not have a specialized UI designer that helped us improve the overall look on the interfaces.

## Accomplishments that we're proud of

We managed to develop an awesome app and API service under 36 hours. Each member of the team contributed greatly to the data and framework design, and helped enhancing the bridge between the Android app and our server.

## What we've learned

How difficult it is to structure a web app from scratch, and how difficult it is to design and develop a full-stack service with limited time and personnel. We understand how much it takes to develop a sustainable and highly-scalable system.

## What's next for Entrée

We plan to complete the review system and menu labelling for easier searches, integrate our service with social networks which will improve on the current people-following idea, and implement a news feed system so that everyone can view the review history of the people they follow. We also aim at providing a web app for users to search, view, contribute, and follow each other right from their web browser.
