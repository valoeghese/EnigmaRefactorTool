// 
// Decompiled by Procyon v0.5.36
// 

package org.objectweb.asm;

@Deprecated
public abstract class RecordComponentVisitor
{
    protected final int api;
    RecordComponentVisitor delegate;
    
    @Deprecated
    public RecordComponentVisitor(final int api) {
        this(api, null);
    }
    
    @Deprecated
    public RecordComponentVisitor(final int api, final RecordComponentVisitor recordComponentVisitor) {
        if (api != 458752 && api != 393216 && api != 327680 && api != 262144 && api != 17301504) {
            throw new IllegalArgumentException("Unsupported api " + api);
        }
        if (api == 17301504) {
            Constants.checkAsm8Experimental(this);
        }
        this.api = api;
        this.delegate = recordComponentVisitor;
    }
    
    @Deprecated
    public RecordComponentVisitor getDelegateExperimental() {
        return this.delegate;
    }
    
    @Deprecated
    public AnnotationVisitor visitAnnotationExperimental(final String descriptor, final boolean visible) {
        if (this.delegate != null) {
            return this.delegate.visitAnnotationExperimental(descriptor, visible);
        }
        return null;
    }
    
    @Deprecated
    public AnnotationVisitor visitTypeAnnotationExperimental(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        if (this.delegate != null) {
            return this.delegate.visitTypeAnnotationExperimental(typeRef, typePath, descriptor, visible);
        }
        return null;
    }
    
    @Deprecated
    public void visitAttributeExperimental(final Attribute attribute) {
        if (this.delegate != null) {
            this.delegate.visitAttributeExperimental(attribute);
        }
    }
    
    @Deprecated
    public void visitEndExperimental() {
        if (this.delegate != null) {
            this.delegate.visitEndExperimental();
        }
    }
}
