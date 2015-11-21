#!/usr/bin/env python
# -*- coding: utf-8 -*-

import 	docx, re , json

# document = docx.opendocx("temp.docx")

# paratextlist = docx.getdocumenttext(document)
# newparatextlist = []
# for paratext in paratextlist:
#     newparatextlist.append(paratext.encode("utf-8"))
# a = '\n'.join(newparatextlist)

fi = open("../temp.txt" , "r")
a = fi.read()


hits = [m.start() for m in re.finditer(r"Câu\b" , a)]
if (len(hits) == 0) hits = [m.start() for m in re.finditer(r"Question\b" , a)]
if (len(hits) == 0) hits = [m.start() for m in re.finditer(r"Q\b" , a)]
Ahits = [m.start() for m in re.finditer(r"A\." , a)]
if (len(Ahits) == 0) Ahits = [m.start() for m in re.finditer(r"a\." , a)]
Bhits = [m.start() for m in re.finditer(r"B\." , a)]
if (len(Bhits) == 0) Bhits = [m.start() for m in re.finditer(r"b\." , a)]
Chits = [m.start() for m in re.finditer(r"C\." , a)]
if (len(Chits) == 0) Chits = [m.start() for m in re.finditer(r"c\." , a)]
Dhits = [m.start() for m in re.finditer(r"D\." , a)]
if (len(Dhits) == 0) Dhits = [m.start() for m in re.finditer(r"d\." , a)]

print hits , Ahits , Bhits , Chits , Dhits

qi = ai = bi = ci = di = 0
nquestion = 0
question = []
Aanswer = []
Banswer = []
Canswer = []
Danswer = []
result = []

while (qi < len(hits)):
	while (hits[qi] >= Ahits[ai]):
		ai+=1
		if (ai == len(Ahits)):
			break

	if (ai == len(Ahits)):
		break;

	while (Ahits[ai] >= Bhits[bi]):
		bi+=1
		if (bi == len(Bhits)):
			break;

	if (bi == len(Bhits)):
		break;

	while (Bhits[bi] >= Chits[ci]):
		ci+=1
		if (ci == len(Chits)):
			break;

	if (ci == len(Chits)):
		break;

	while (Chits[ci] >= Dhits[di]):
		di+=1
		if (di == len(Dhits)):
			break;

	if (di == len(Dhits)):
		break;

	tres = {}

	tres["question"] =  a[hits[qi]:Ahits[ai]].decode("utf-8")
	tres["answer"] = []
	tres["answer"].append(a[Ahits[ai]+2:Bhits[bi]].decode("utf-8"))
	tres["answer"].append(a[Bhits[bi]+2:Chits[ci]].decode("utf-8"))
	tres["answer"].append(a[Chits[ci]+2:Dhits[di]].decode("utf-8"))
	
	while (Dhits[di] >= hits[qi]):
		qi+=1
		if (qi == len(hits)):
			break;

	if (qi == len(hits)):
		tres["answer"].append(a[Dhits[di]+2:len(a)].decode("utf-8"))
	else:
		tres["answer"].append(a[Dhits[di]+2:hits[qi]].decode("utf-8"))

	result.append(tres)

json_data = json.dumps(result, ensure_ascii=False , sort_keys=True, indent=4, separators=(',', ': ')).encode("utf-8")

fo = open("../output.txt" , "w")
fo.write(json_data)