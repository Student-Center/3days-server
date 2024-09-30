package com.threedays.domain.user.repository

import com.threedays.domain.user.entity.Job
import com.threedays.support.common.base.domain.QueryRepository

interface JobQueryRepository : QueryRepository<Job, Job.Id>
