package com.zachcalvert.picturescript.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.model.FolderBase;
import com.zachcalvert.picturescript.repository.FileRepository;
import com.zachcalvert.picturescript.state.dto.ProcessedFile;
import com.zachcalvert.picturescript.state.dto.ProcessedFolderState;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProcessedFileStateManagementService {

  private FileRepository fileRepository;

  private List<ProcessedFolderLoadListener> listeners;

  @Autowired
  public ProcessedFileStateManagementService(
      FileRepository fileRepository,
      Optional<List<ProcessedFolderLoadListener>> listeners) {
    this.fileRepository = fileRepository;
    this.listeners = listeners.orElseGet(ArrayList::new);
  }

  public ProcessedFolderState extractState(FolderBase base) {
    List<File> files = fileRepository.findFilesByFolderBase(base);
    ArrayList<ProcessedFile> processedFiles = new ArrayList<>();
    for (File file:files) {
      ProcessedFile processedFile = new ProcessedFile(base, file);
      processedFiles.add(processedFile);
    }
    ProcessedFolderState result = new ProcessedFolderState();
    result.setProcessedFiles(processedFiles);
    return result;
  }

  public ProcessedFolderState readStateFromFile(Path file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(file.toFile(), ProcessedFolderState.class);
  }

  public void writeStateToFile(Path file, ProcessedFolderState state) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file.toFile(), state);
  }

  public void attemptLoadPreviouslyProcessedDirectory(FolderBase folderBase, boolean forExport) {
    Path previouslyProcessed = Paths.get(folderBase.getPath(), ProcessedFolderState.DEFAULT_FILE_NAME);
    if (previouslyProcessed.toFile().exists()) {
      try {
        loadFromState(folderBase, readStateFromFile(previouslyProcessed), forExport);
      } catch (Exception e) {
        throw new RuntimeException("Failed to load previous state from folder: " + previouslyProcessed.toString(), e);
      }
    }
  }

  @Transactional
  public void loadFromState(FolderBase base, ProcessedFolderState state, boolean availableForExport) {
    for (ProcessedFile processedFile:state.getProcessedFiles()) {
      Path path = Paths.get(base.getPath(), processedFile.getRelativePath());
      if (!path.toFile().exists()) {
        listeners.stream().forEach(listener -> listener.fileMissingFromLoad(base, path, processedFile));
        continue;
      }
      File found = fileRepository.findFileByPath(path.toString());
      if (found != null) {
        listeners.stream().forEach(listener -> listener.fileAlreadyExists(base, processedFile, found));
      } else {
        File file = new File(availableForExport, processedFile.getSha256(), path.toString(), processedFile.getDateCreated().toInstant(), processedFile.getEarliestKnownDate().toInstant(), processedFile.getExtension(), base, processedFile.getOriginalFileName());
        fileRepository.save(file);
        listeners.stream().forEach(listener -> listener.fileLoadedFromState(base, processedFile, found));
      }
    }
  }
}
