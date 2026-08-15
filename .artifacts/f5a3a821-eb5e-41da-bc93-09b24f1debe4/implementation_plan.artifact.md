# Navigation & Full Bible Reading Experience

Implement a multi-screen navigation flow to allow users to navigate from Books to Chapters and finally to the Verse reading screen.

## User Review Required

> [!IMPORTANT]
> I will be adding `androidx.navigation:navigation-compose`. I will also introduce a mapping for Book Numbers to Names (e.g., Book 1 -> Genesis) since the JSON only contains numbers.

## Proposed Changes

### Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/Users/user/AndroidStudioProjects/TenglishBible2/gradle/libs.versions.toml)
- Add `navigationCompose = "2.8.8"` to versions.
- Add `androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }` to libraries.

#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/user/AndroidStudioProjects/TenglishBible2/app/build.gradle.kts)
- Add `implementation(libs.androidx.navigation.compose)` to dependencies.

### Data & Logic

#### [NEW] [BibleUtils.kt](file:///C:/Users/user/AndroidStudioProjects/TenglishBible2/app/src/main/java/com/tenglishbible/tenglishbible/BibleUtils.kt)
- Create a helper to map book numbers to their English/Tenglish names.

#### [MODIFY] [BibleViewModel.kt](file:///C:/Users/user/AndroidStudioProjects/TenglishBible2/app/src/main/java/com/tenglishbible/tenglishbible/BibleViewModel.kt)
- Add `getChapters(bookId: Int)` and `getVerses(bookId: Int, chapterId: Int)` functions using `suspend` or `Flow`.

### UI & Navigation

#### [MODIFY] [MainActivity.kt](file:///C:/Users/user/AndroidStudioProjects/TenglishBible2/app/src/main/java/com/tenglishbible/tenglishbible/MainActivity.kt)
- Replace `BooksScreen` direct call with a `NavHost`.
- Update `BooksScreen` to handle clicks and navigate to `ChaptersScreen`.
- Implement `ChaptersScreen`.
- Implement `VersesScreen`.

## Verification Plan

### Manual Verification
- Deploy to device.
- Verify that clicking "Genesis" (Book 1) shows chapters.
- Verify that clicking "Chapter 1" shows the correct verses.
- Verify that the Back button works correctly between screens.
