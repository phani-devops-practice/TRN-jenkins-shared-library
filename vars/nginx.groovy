def call() {
  node() {
    common.pipelineINIT()

    if (env.BRANCH_NAME == env.TAG_NAME)
    {
      common.publishArtifacts()
    }
  }
}

