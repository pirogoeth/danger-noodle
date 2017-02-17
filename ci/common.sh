#!/usr/bin/env bash

function wprint() {
    desc=$1
    detail=$2
    [[ -z "${detail}" ]] && eol="\r" || eol="\n"
    [[ ! -z "${detail}" ]] || detail="..."

    printf " %-50s [ %s ]${eol}" "${desc}" "${detail}"
}
