package
tk
.
valoeghese
.
ertool;

import java
   .util
      .Locale;

public
enum
RefactorType
{
	PACKAGE
,CLASSNAME
;

public
static
final
RefactorType
get
(
		String
	type
)
{
	return
valueOf(type
.toUpperCase(
Locale
					.ROOT
				));
	}
}
