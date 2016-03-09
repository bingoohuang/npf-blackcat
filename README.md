# npf-blackcat
blackcat support for NPF project

# How to install
  1. Put ibatis-enhanced before ibatis in pom.xml dependencies.
  2. Put npf-blackcat before springweb, esfservicecommon, eop framework in pom.xml dependencies.
  3. Do not depend on npf-mock, which is for debugging and compiling.

# How to config sensitive filter in log 
  1. Use EsfProperties config system.
  1. add `Blackcat.Unsensitive.Config=[content]` to config file.
  2. Content should be in JSON array format, like `[{pattern:"(?<=PASSWORD:)[^\s]+"}, {pattern:"(?is)(?<=<PASSWORD>).*(?=</PASSWORD>)"}]`
  3. More examples: `[{pattern:"(\\d{3})\\d{3}(?:19|20)\\d{2}(?:0|1)\\d(?:[0123])\\d{2}(\\d{3})", replace="$1***$2"}]`
  3. The default replace string is `***`, if you want to customize it, add replace value in json, like  `[{pattern:"(?<=PASSWORD:)[^\s]+"}, replace="###"}]`.
  4. Add `Blackcat.SQLResult.MaxRows=5` to config file to ignore too many results in SQL query.
