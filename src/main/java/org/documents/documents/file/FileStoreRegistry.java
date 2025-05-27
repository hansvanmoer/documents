package org.documents.documents.file;

import org.documents.documents.file.impl.FileContentImpl;

import java.util.UUID;

public interface FileStoreRegistry {

    FileStore getFileStore(FileStoreType fileStoreType);

    TransformFileStore getTransformFileStore();

    default UUID copyToTransformFileStore(FileReference fileReference, String mimeType) {
        return getFileStore(fileReference.fileStoreType()).copyTo(
                fileReference.uuid(),
                mimeType,
                getTransformFileStore()
        );
    }

    default FileContent createFileProxy(FileReference fileReference) {
        return new FileContentImpl(getFileStore(fileReference.fileStoreType()), fileReference.uuid());
    }
}
