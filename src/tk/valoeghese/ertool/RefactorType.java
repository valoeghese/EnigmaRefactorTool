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
	PACKAGE,
	PACKAGE_PLUS
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
	type
= 	type
				.
toUpperCase(
	Locale
						.ROOT
)
;
	return type
.equals
							(
		"PACKAGE+"
)?
		PACKAGE_PLUS: valueOf(type)
		;
	}
}
