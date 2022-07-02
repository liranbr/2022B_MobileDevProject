# 2022B_MobileDevProject
Project for Mobile Development course - 2022B Android, 
by Ronen Gurevich and Liran Braverman.

An app for indoor/on-location navigation.
With the following features:
	Register/Login
	Look Around mode
		Traverse the StreetView-like images in 4 directions
	Deep links for GPS navigation
	Directions mode
		On top of Look Around mode,
		it calculates the shortest path from source to destination,
		and instructs the user to navigate it with 
			button highlighting
			voice instructions
	Report system
	Multi-floor plan view
	
Firebase:
	Accounts through Firebase Auth
	All information about the location is saved in Firebase Firestore
	All photos are downloaded from and uploaded to Firebase Storage

*Didn't implement:
	Drawing dynamic path on floor plan view
	Travel time estimation

*Mapped out a part of Afeka College's Fikus building, for proof-of-concept
