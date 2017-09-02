# PictureScript

 _This utility is under heavy construction._

This Spring Boot organized Java application is meant to crawl input directories, calculate thie file 
sha-256 checksums, and treat those check-sum collisions as file duplicates.  Output is delivered
to configured output directories, or potentially re-organized based on the output naming.  

## Use Cases

I as a user want to organize my images into a date-structured folder format in order to organize my 
family photos.

I as a user want to organize my movies into a date-structured folder format in order to organize my
family movies.

## Planned Features

I hope to slap a REST API in between the image loading phase and the image delivery phase where
we can view photos, add image meta data, edit exif data, or flag for permanent deletion.

# Execution Structure

## Execution Overview

The execution of this utility is organized into phases.  The phases are event driven, using the 
internal Spring Boot eventing utilities.  The goal for this approach was to decouple the execution
steps and to make input processing and output processing to support easy threading after 
basic program structure is complete.  

## Phases

1. Load
1. Identify output
1. Construct output plan
  1. Copy instructions
  1. Move instructions
1. Dry run of plan
1. Real run of plan

### Load

Extract any and all input and existing output.  Only input is treated as desired modification for
the output directories.  For example, if I have input folder I, and outfolders O1 and O2, if O2
contains an image that O1 does not have, then O1 will not be updated with the image.  

### Identify output

Output will be identified by making use of the in-RAM database, H2.  Types (file types based off of
file extension) will be used to determine files applicable for this output configuration.

### Construct output plan

A plan is meant to be non-destructive of existing files, but it may move them according to newest
planned file path of date/time.  The goal of this phase is to identify all commands required to copy 
input to the output directory, or to move file from output A to output A'. Moves are only applicable
to files that should be on the output directory, and already exist on the output directory in a 
different location.

### Dry run of the plan

A safety check execution that runs a RAM dry-run, checking for collisions and destructive commands.  

### Real run of plan

Execute the copy and move commands

# Code Style

Using Google's Java format XML from <https://github.com/google/styleguide>

# Test Image Resources

<https://openclipart.org/detail/285017/gasoline>
<https://openclipart.org/detail/285013/another-basketball>
<https://openclipart.org/detail/285194/death-head-hawk>