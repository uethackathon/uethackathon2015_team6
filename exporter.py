#!/usr/bin/env python
# -*- coding: utf-8 -*-

import docx , json

document = docx.Document()

fi = open("../input.txt" , "r")

s = fi.read()

test = json.JSONDecoder().decode(s)

document.add_heading(test["header"] , 0)
test = test["questions"]

for q in test:
	ss = q["text"]
	for i in range(len(q["choices"])):
		ss += chr(ord('A') + i) + ". " + q["choices"][i]["text"]

	document.add_paragraph(ss)
	
document.add_page_break()

ss = u"Đáp án:\n"
for i in range(len(test)):
	ss += str(i+1) + ". "
	for j in range(len(test[i]["choices"])):
		if (test[i]["choices"][j]["correct"]):
			ss += chr(ord("A") + j) + " "
	ss += '\n'

document.add_paragraph(ss)

document.save('../test.docx')