# TeamM-Diary

2022.04.08 Friday 3:00 PM - 8:00 PM
Attendance: Weina Dai, Zehua Wang, Shuchen Zhang, Liyuan Guo
Goals: After we separate tasks into different groups and individually work them in progress, we meet this time to follow up with progress and make adjustments about unfinished tasks. Originally, we separate the tasks into three major parts: Shuchen for SignIn and SignUp in the first page, Liyuan for Recent fragment and Navigation drawer, and Weina and Bella for New/Edit, upload photo and input mood fragments.
Progress conclusion: Complete most parts of Sign in and Sign up, but still need to connect to firebase correctly and adjust that for the further fragments. The Recent Fragment has more work to do than expected. And New/Edit activity needs the firebase from sign in to further store the texts, and finish most parts of the New/Edit activity and upload image fragments.
Re-separate parts to finish before ddl: Weina finish the rest part of New/Edit Activity, Bella and Liyuan try to do more of Recent, including adapter, layouts, card view and list view, and if the search activity is quite hard this time, we can push that to the next sprint. (At last, Weina checked about all parts, and write codes to combine all of them)



Details and features completed this time:

Sign up with email, username and password. (Save these data to database for further sign in)

Sign in with already existed accounts, or show toasts for failed authentication.

Navigation drawer to all of our fragments, my tags (empty now), statistics (empty now), Recent (implemented this time), and my diary(empty now).

Recent Fragment shows the most recent ten diaries by the order of time, with the title, date time and the mood saved for each entry. We used card view and list view, and adapter to show the list. For this sprint, we have not set up the queries so the it will show all entries in the database. Currenly, each entry has a unique id, which will be rest to 0 after each luanch, causing the app to overwrite previous entries. We will fix it in the next sprint.  In this, we add a search button, but not implement functions this time.

A floating button in each fragment or activity to get to the New activity.

New activity: the main part to input text and save in database, and on the left part, the Tags can be clicked to show the tags layout (functions not implemented), on the bottom to choose the mood for the current diary with a toast text, on the top to choose upload image, which get to the next fragment, and save button to save all of the current data.

UploadImage fragment: users can click in the middle to upload one photo from users gallery, and use that selected photo as the background for overview, and also users can input text as title for that photo.

General state of app and other functions to implement next time:
We approximately did half functions of our app and already set up our database for each users. But there’re still other functions to implement, including statistics fragment, my tags fragment (and also the functions to add tags in new diary), and a calendar view for the my diary fragment, and the search function in Recent Fragment.



Because our app is not for interactions between users and mostly for users to record their own data, thus we didn’t create a large list of users.

zwang258@jh.edu 123456
zwang258@jhu.edu 123456
lguo21@jh.edu 123456
lguo21@jhu.edu 123456
wdai11@jhu.edu password
123456@jh.edu 123456
shuchen@jh.edu 123456
shuchen@jhu.edu 123456


Sprint 2/3 Update:
Finished implementing all expected features except the delete image feature. Since we implemented the add tag function using a pop-up dialogue within the New Entry Activity, so we did not implement the My Tag feature that was originally planned for adding tags. Users can now search by tags (for seaching with multiple tags, separate them with white space). Notice that we use fuzzy matching for each tag. 
