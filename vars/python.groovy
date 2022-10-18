def call() {
  env.EXTRA_OPTS=""
  node() {
    common.pipelineINIT()

    common.codeChecks()
    if (env.BRANCH_NAME == env.TAG_NAME)
    {
      common.publishArtifacts()
    }
  }
}

