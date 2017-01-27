Alfresco 5.1.g-patched
======================

This is a patched version of Alfresco 5.1.g. The following changes have been made.

* Don't build the module `legacy-lucene`.
* Patched `QuickShareServiceImpl.java` to work correctly in a multi-tenant setup.
* ALF-21521: Disable the Global Authentication Filter.

You can create a diff to the tag `5.1.g` by executing 

    $ ./diff-to-tag.sh

This will create a file `../community-edition-5.1.g.diff`.
